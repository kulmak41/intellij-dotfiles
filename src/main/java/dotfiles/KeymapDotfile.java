package dotfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.KeymapManager;

import java.io.File;
import java.io.IOException;

public class KeymapDotfile extends Dotfile {
    private static final KeymapDotfile instance = new KeymapDotfile();

    private KeymapDotfile() {
    }

    public static KeymapDotfile getInstance() {
        return instance;
    }

    private static final Logger LOG = Logger.getInstance(DotfilesStartupActivity.class);

    private static class ShortcutAction {
        private String shortcut;
        private String action;

        public String getShortcut() {
            return shortcut;
        }

        public String getAction() {
            return action;
        }
    }

    @Override
    public String getPath() {
        return directory + File.separator + "keymap.json";
    }

    @Override
    public void apply() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(System.getProperty("user.home") + File.separator + ".idea" + File.separator + "keymap.json");
        try {
            ShortcutAction[] shortcuts = mapper.readValue(file, ShortcutAction[].class);
            var keymap = KeymapManager.getInstance().getActiveKeymap();
            for (var shortcutAction : shortcuts) {
                KeyboardShortcut keyboardShortcut = KeyboardShortcut.fromString(shortcutAction.getShortcut());

                var prevActions = keymap.getActionIds(keyboardShortcut);
                for (var id : prevActions) {
                    keymap.removeShortcut(id, keyboardShortcut);
                }

                keymap.addShortcut(shortcutAction.getAction(), keyboardShortcut);
            }

            LOG.info("Settings from the keymap dotfile have been applied successfully");
        } catch (IOException e) {
            LOG.info("An error occurred in the process of parsing settings from the keymap dotfile");
        }
    }
}
