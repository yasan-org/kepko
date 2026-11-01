package glass.yasan.crowdin.screenshots

import java.io.File
import kotlin.system.exitProcess

private const val USAGE = """Uploads snapshot PNGs to Crowdin and tags each with the source strings it shows.

Usage:
  crowdin-screenshots --namespace <name> <snapshot dir>...
  crowdin-screenshots --dry-run --strings <strings.xml> [--strings <strings.xml>]... <snapshot dir>...

Options:
  --namespace <name>  Prefix of the uploaded screenshot names. Only screenshots in this namespace are
                      deleted when stale, so separate uploads to one project need separate names.
  --dry-run           Prints the tags without Crowdin, and fails on keys missing from --strings.
  --strings <file>    Source strings.xml to resolve keys against in a dry run.

Environment:
  CROWDIN_PERSONAL_TOKEN  Personal access token.
  CROWDIN_PROJECT_ID      Numeric project ID.
  CROWDIN_SOURCE_FILE     Path of the source strings file, matched against the end of the file
                          paths in Crowdin.
  CROWDIN_BRANCH          Optional Crowdin branch name, when the file exists in several branches.
  CROWDIN_BASE_URL        Optional, defaults to https://api.crowdin.com/api/v2."""

private val requiredEnvironment = listOf("CROWDIN_PERSONAL_TOKEN", "CROWDIN_PROJECT_ID", "CROWDIN_SOURCE_FILE")

private class Arguments(
    val dryRun: Boolean,
    val namespace: String?,
    val stringFiles: List<File>,
    val directories: List<File>,
)

private fun parseArguments(args: Array<String>): Arguments {
    var dryRun = false
    var namespace: String? = null
    val stringFiles = mutableListOf<File>()
    val directories = mutableListOf<File>()
    val remaining = args.iterator()
    while (remaining.hasNext()) {
        when (val arg = remaining.next()) {
            "--dry-run" -> dryRun = true
            "--namespace" -> namespace = remaining.nextOrNull() ?: exit(USAGE)
            "--strings" -> stringFiles += File(remaining.nextOrNull() ?: exit(USAGE))
            else -> if (arg.startsWith("-")) exit("Unknown option $arg\n\n$USAGE") else directories += File(arg)
        }
    }
    val valid = directories.isNotEmpty() && if (dryRun) stringFiles.isNotEmpty() else !namespace.isNullOrBlank()
    if (!valid) exit(USAGE)
    return Arguments(dryRun, namespace, stringFiles, directories)
}

private fun Iterator<String>.nextOrNull(): String? = if (hasNext()) next() else null

public fun main(args: Array<String>) {
    val arguments = parseArguments(args)
    val log: (String) -> Unit = ::println
    val succeeded = try {
        if (arguments.dryRun) {
            val strings = readAndroidStrings(arguments.stringFiles)
            syncScreenshots(arguments.directories, strings, api = null, namespace = "", strict = true, log = log)
        } else {
            val missing = requiredEnvironment.filter { System.getenv(it).isNullOrEmpty() }
            if (missing.isNotEmpty()) exit("Missing environment variables: ${missing.joinToString()}")
            val api = CrowdinClient(
                token = System.getenv("CROWDIN_PERSONAL_TOKEN"),
                projectId = System.getenv("CROWDIN_PROJECT_ID"),
                baseUrl = System.getenv("CROWDIN_BASE_URL").takeUnless { it.isNullOrEmpty() }
                    ?: "https://api.crowdin.com/api/v2",
                sourceFile = System.getenv("CROWDIN_SOURCE_FILE"),
                branch = System.getenv("CROWDIN_BRANCH").takeUnless { it.isNullOrEmpty() },
                log = log,
            )
            syncScreenshots(
                directories = arguments.directories,
                strings = api.sourceStrings(),
                api = api,
                namespace = requireNotNull(arguments.namespace),
                strict = false,
                log = log,
            )
        }
    } catch (error: CrowdinException) {
        exit(error.message.orEmpty())
    }
    if (!succeeded) exitProcess(1)
}

private fun exit(message: String): Nothing {
    System.err.println(message)
    exitProcess(1)
}
