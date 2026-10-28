#!/usr/bin/env bash

# Bumps the version, tags it and creates a GitHub release whose notes list
# the pull requests and commits since the previous release.
#
# Usage: scripts/release.sh
#
# Environment:
#   BUMP     patch, minor or major (default: patch)
#   FORCE    true to release even if nothing changed since the last release
#   DRY_RUN  true to only print the next version and its release notes
#   GH_TOKEN token for the GitHub CLI; releases it creates trigger other workflows

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
BUILD_FILE="$PROJECT_DIR/build.gradle.kts"

BUMP="${BUMP:-patch}"
FORCE="${FORCE:-false}"
DRY_RUN="${DRY_RUN:-false}"

case "$BUMP" in
  patch | minor | major) ;;
  *) echo "❌ Error: unknown bump '$BUMP' (use patch, minor or major)"; exit 1 ;;
esac

cd "$PROJECT_DIR"

REPO="${GITHUB_REPOSITORY:-$(gh repo view --json nameWithOwner --jq .nameWithOwner)}"
REPO_URL="https://github.com/$REPO"

summary() {
  echo "$1"
  if [[ -n "${GITHUB_STEP_SUMMARY:-}" ]]; then
    echo "$1" >>"$GITHUB_STEP_SUMMARY"
  fi
}

CURRENT="$(sed -nE 's/^ *version = "([0-9]+\.[0-9]+\.[0-9]+)"$/\1/p' "$BUILD_FILE")"
if [[ -z "$CURRENT" ]]; then
  echo "❌ Error: no version found in $BUILD_FILE"
  exit 1
fi

if ! git rev-parse -q --verify "refs/tags/$CURRENT" >/dev/null; then
  echo "❌ Error: the current version $CURRENT has no tag to compare against"
  exit 1
fi

# Writes release notes for the commits in <range> to <file>, comparing <from> to <to>.
write_notes() {
  local file="$1" range="$2" from="$3" to="$4"
  local commits pull_requests=""
  commits="$(git log --reverse --format='%H%x09%s' "$range")"

  # Pull requests merged into each commit, oldest first and listed once.
  while IFS=$'\t' read -r sha _; do
    [[ -z "$sha" ]] && continue
    pull_requests+="$(gh api "repos/$REPO/commits/$sha/pulls" \
      --jq '.[] | select(.merged_at != null) | "\(.number)\t\(.title)\t\(.html_url)"')"$'\n'
  done <<<"$commits"
  pull_requests="$(awk -F'\t' 'NF && !seen[$1]++' <<<"$pull_requests")"

  {
    echo "## Pull requests"
    echo
    if [[ -n "$pull_requests" ]]; then
      while IFS=$'\t' read -r number title url; do
        echo "- $title ([#$number]($url))"
      done <<<"$pull_requests"
    else
      echo "No pull requests were merged in this release."
    fi
    echo
    echo "## Commits"
    echo
    if [[ -n "$commits" ]]; then
      while IFS=$'\t' read -r sha subject; do
        echo "- [\`${sha:0:7}\`]($REPO_URL/commit/$sha) $subject"
      done <<<"$commits"
    else
      echo "No commits since $from."
    fi
    echo
    echo "**Full Changelog**: $REPO_URL/compare/$from...$to"
  } >"$file"
}

# A run that pushed its tag but failed to create the release leaves the current
# version without a release. Create it instead of bumping again, so the Publish
# workflow still runs for that version.
if ! RELEASE_ERROR="$(gh api "repos/$REPO/releases/tags/$CURRENT" --silent 2>&1)"; then
  if [[ "$RELEASE_ERROR" != *"HTTP 404"* ]]; then
    echo "❌ Error: could not check for a release of $CURRENT: $RELEASE_ERROR"
    exit 1
  fi

  PREVIOUS="$(git describe --tags --abbrev=0 --match '[0-9]*.[0-9]*.[0-9]*' "$CURRENT^" 2>/dev/null || true)"
  if [[ -z "$PREVIOUS" ]]; then
    echo "❌ Error: $CURRENT has no release and no earlier version tag to write its notes from"
    exit 1
  fi
  # Leave out the version bump commit, as a normal release does.
  RANGE="$PREVIOUS..$CURRENT"
  if [[ "$(git log -1 --format=%s "$CURRENT")" == "Bump version to $CURRENT" ]]; then
    RANGE="$PREVIOUS..$CURRENT^"
  fi

  NOTES="$(mktemp)"
  write_notes "$NOTES" "$RANGE" "$PREVIOUS" "$CURRENT"

  if [[ "$DRY_RUN" == "true" ]]; then
    summary "Dry run: $CURRENT is tagged but has no release, so it would create that release (from $PREVIOUS)."
    summary ""
    summary "$(cat "$NOTES")"
    exit 0
  fi

  gh release create "$CURRENT" --repo "$REPO" --title "$CURRENT" --notes-file "$NOTES" --verify-tag
  summary "Released [$CURRENT]($REPO_URL/releases/tag/$CURRENT), which was tagged but had no release. Run again for a new version."
  exit 0
fi

if [[ -z "$(git log --format=%H "$CURRENT..HEAD")" && "$FORCE" != "true" ]]; then
  summary "No changes since $CURRENT, so nothing was released."
  exit 0
fi

IFS=. read -r MAJOR MINOR PATCH <<<"$CURRENT"
case "$BUMP" in
  major) NEXT="$((MAJOR + 1)).0.0" ;;
  minor) NEXT="$MAJOR.$((MINOR + 1)).0" ;;
  patch) NEXT="$MAJOR.$MINOR.$((PATCH + 1))" ;;
esac

if git rev-parse -q --verify "refs/tags/$NEXT" >/dev/null; then
  echo "❌ Error: tag $NEXT already exists"
  exit 1
fi

NOTES="$(mktemp)"
write_notes "$NOTES" "$CURRENT..HEAD" "$CURRENT" "$NEXT"

if [[ "$DRY_RUN" == "true" ]]; then
  summary "Dry run: would release $NEXT (from $CURRENT)."
  summary ""
  summary "$(cat "$NOTES")"
  exit 0
fi

sed -i.bak -E "s/^( *version = \")$CURRENT(\")$/\1$NEXT\2/" "$BUILD_FILE"
rm "$BUILD_FILE.bak"

git add "$BUILD_FILE"
git commit -m "Bump version to $NEXT"
git tag -a "$NEXT" -m "$NEXT"
git push --atomic origin HEAD:main "refs/tags/$NEXT"

gh release create "$NEXT" --repo "$REPO" --title "$NEXT" --notes-file "$NOTES" --verify-tag

summary "Released [$NEXT]($REPO_URL/releases/tag/$NEXT)."
