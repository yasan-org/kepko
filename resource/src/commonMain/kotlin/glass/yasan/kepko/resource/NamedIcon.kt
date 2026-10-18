package glass.yasan.kepko.resource

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

public enum class NamedIcon(
    public val id: String,
    public val painter: @Composable () -> Painter,
) {
    ADD(
        id = "add",
        painter = { Icons.add }
    ),
    ARCHIVE(
        id = "archive",
        painter = { Icons.archive }
    ),
    ATTACH_FILE(
        id = "attach_file",
        painter = { Icons.attachFile }
    ),
    ATTACHMENT(
        id = "attachment",
        painter = { Icons.attachment }
    ),
    AUTO_AWESOME(
        id = "auto_awesome",
        painter = { Icons.autoAwesome }
    ),
    AUTORENEW(
        id = "autorenew",
        painter = { Icons.autorenew }
    ),
    BUG_REPORT(
        id = "bug_report",
        painter = { Icons.bugReport }
    ),
    BUILD(
        id = "build",
        painter = { Icons.build }
    ),
    CACHED(
        id = "cached",
        painter = { Icons.cached }
    ),
    CAKE(
        id = "cake",
        painter = { Icons.cake }
    ),
    CELEBRATION(
        id = "celebration",
        painter = { Icons.celebration }
    ),
    CHANGE_CIRCLE(
        id = "change_circle",
        painter = { Icons.changeCircle }
    ),
    CHECK(
        id = "check",
        painter = { Icons.check }
    ),
    CHEVRON_BACKWARD(
        id = "chevron_backward",
        painter = { Icons.chevronBackward }
    ),
    CHEVRON_DOWN(
        id = "chevron_down",
        painter = { Icons.chevronDown }
    ),
    CHEVRON_FORWARD(
        id = "chevron_forward",
        painter = { Icons.chevronForward }
    ),
    CLOSE(
        id = "close",
        painter = { Icons.close }
    ),
    CLOUD_SYNC(
        id = "cloud_sync",
        painter = { Icons.cloudSync }
    ),
    CODE(
        id = "code",
        painter = { Icons.code }
    ),
    CONTENT_COPY(
        id = "content_copy",
        painter = { Icons.contentCopy }
    ),
    CONTENT_CUT(
        id = "content_cut",
        painter = { Icons.contentCut }
    ),
    CONTENT_PASTE(
        id = "content_paste",
        painter = { Icons.contentPaste }
    ),
    DELETE(
        id = "delete",
        painter = { Icons.delete }
    ),
    DIRECTORY_SYNC(
        id = "directory_sync",
        painter = { Icons.directorySync }
    ),
    DOWNLOAD(
        id = "download",
        painter = { Icons.download }
    ),
    EDIT(
        id = "edit",
        painter = { Icons.edit }
    ),
    ERROR(
        id = "error",
        painter = { Icons.error }
    ),
    EXPERIMENT(
        id = "experiment",
        painter = { Icons.experiment }
    ),
    EXTENSION(
        id = "extension",
        painter = { Icons.extension }
    ),
    EXTENSION_OFF(
        id = "extension_off",
        painter = { Icons.extensionOff }
    ),
    FAVORITE(
        id = "favorite",
        painter = { Icons.favorite }
    ),
    FILTER_BW(
        id = "filter_bw",
        painter = { Icons.filterBw }
    ),
    FOLDER(
        id = "folder",
        painter = { Icons.folder }
    ),
    FOLDER_COPY(
        id = "folder_copy",
        painter = { Icons.folderCopy }
    ),
    FORMAT_QUOTE(
        id = "format_quote",
        painter = { Icons.formatQuote }
    ),
    HISTORY(
        id = "history",
        painter = { Icons.history }
    ),
    HOME(
        id = "home",
        painter = { Icons.home }
    ),
    INFO(
        id = "info",
        painter = { Icons.info }
    ),
    LIGHT_MODE(
        id = "light_mode",
        painter = { Icons.lightMode }
    ),
    LOCK(
        id = "lock",
        painter = { Icons.lock }
    ),
    LOCK_OPEN(
        id = "lock_open",
        painter = { Icons.lockOpen }
    ),
    MAIL(
        id = "mail",
        painter = { Icons.mail }
    ),
    MANAGE_HISTORY(
        id = "manage_history",
        painter = { Icons.manageHistory }
    ),
    MEMORY(
        id = "memory",
        painter = { Icons.memory }
    ),
    MENU(
        id = "menu",
        painter = { Icons.menu }
    ),
    MIC(
        id = "mic",
        painter = { Icons.mic }
    ),
    MODEL_TRAINING(
        id = "model_training",
        painter = { Icons.modelTraining }
    ),
    MODE_NIGHT(
        id = "mode_night",
        painter = { Icons.modeNight }
    ),
    MODE_OFF_ON(
        id = "mode_off_on",
        painter = { Icons.modeOffOn }
    ),
    MUSIC_NOTE(
        id = "music_note",
        painter = { Icons.musicNote }
    ),
    NEW_WINDOW(
        id = "new_window",
        painter = { Icons.newWindow }
    ),
    OPEN_IN_BROWSER(
        id = "open_in_browser",
        painter = { Icons.openInBrowser }
    ),
    OPEN_IN_NEW(
        id = "open_in_new",
        painter = { Icons.openInNew }
    ),
    PALETTE(
        id = "palette",
        painter = { Icons.palette }
    ),
    PAUSE(
        id = "pause",
        painter = { Icons.pause }
    ),
    PERSON(
        id = "person",
        painter = { Icons.person }
    ),
    PLAY_ARROW(
        id = "play_arrow",
        painter = { Icons.playArrow }
    ),
    PRIVACY_TIP(
        id = "privacy_tip",
        painter = { Icons.privacyTip }
    ),
    PUBLIC(
        id = "public",
        painter = { Icons.public }
    ),
    PUBLIC_OFF(
        id = "public_off",
        painter = { Icons.publicOff }
    ),
    REFRESH(
        id = "refresh",
        painter = { Icons.refresh }
    ),
    RELEASE_ALERT(
        id = "release_alert",
        painter = { Icons.releaseAlert }
    ),
    REPORT(
        id = "report",
        painter = { Icons.report }
    ),
    RESET_SETTINGS(
        id = "reset_settings",
        painter = { Icons.resetSettings }
    ),
    RESTART_ALT(
        id = "restart_alt",
        painter = { Icons.restartAlt }
    ),
    ROCKET(
        id = "rocket",
        painter = { Icons.rocket }
    ),
    ROCKET_LAUNCH(
        id = "rocket_launch",
        painter = { Icons.rocketLaunch }
    ),
    SAVE(
        id = "save",
        painter = { Icons.save }
    ),
    SEARCH(
        id = "search",
        painter = { Icons.search }
    ),
    SEND(
        id = "send",
        painter = { Icons.send }
    ),
    SETTINGS(
        id = "settings",
        painter = { Icons.settings }
    ),
    SETTINGS_BACKUP_RESTORE(
        id = "settings_backup_restore",
        painter = { Icons.settingsBackupRestore }
    ),
    SHAPE_LINE(
        id = "shape_line",
        painter = { Icons.shapeLine }
    ),
    SHAPES(
        id = "shapes",
        painter = { Icons.shapes }
    ),
    SKIP_NEXT(
        id = "skip_next",
        painter = { Icons.skipNext }
    ),
    SKIP_PREVIOUS(
        id = "skip_previous",
        painter = { Icons.skipPrevious }
    ),
    SORT(
        id = "sort",
        painter = { Icons.sort }
    ),
    STAR(
        id = "star",
        painter = { Icons.star }
    ),
    SUPPORT(
        id = "support",
        painter = { Icons.support }
    ),
    SYNC(
        id = "sync",
        painter = { Icons.sync }
    ),
    SYNC_DISABLED(
        id = "sync_disabled",
        painter = { Icons.syncDisabled }
    ),
    SYNC_PROBLEM(
        id = "sync_problem",
        painter = { Icons.syncProblem }
    ),
    THUMB_DOWN(
        id = "thumb_down",
        painter = { Icons.thumbDown }
    ),
    THUMB_UP(
        id = "thumb_up",
        painter = { Icons.thumbUp }
    ),
    UPDATE(
        id = "update",
        painter = { Icons.update }
    ),
    UPLOAD(
        id = "upload",
        painter = { Icons.upload }
    ),
    VERIFIED(
        id = "verified",
        painter = { Icons.verified }
    ),
    VISIBILITY(
        id = "visibility",
        painter = { Icons.visibility }
    ),
    VISIBILITY_OFF(
        id = "visibility_off",
        painter = { Icons.visibilityOff }
    ),
    WAND_STARS(
        id = "wand_stars",
        painter = { Icons.wandStars }
    ),
    WARNING(
        id = "warning",
        painter = { Icons.warning }
    ),
    ;

    public companion object {
        public fun fromIdOrNull(id: String): NamedIcon? = entries.firstOrNull { it.id == id }
    }
}
