import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import dotfiles.KeymapDotfile;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class KeymapDotfileTest extends BasePlatformTestCase {
    public void testApply() throws FileNotFoundException {
        KeymapDotfile keymapDotfile = KeymapDotfile.getInstance();

        PrintWriter writer = new PrintWriter(keymapDotfile.getPath());
        writer.print("""
                [
                    {"shortcut": "control S", "action": "Replace"}
                ]""");
        writer.close();

        keymapDotfile.apply();
        var keymap = KeymapManager.getInstance().getActiveKeymap();
        KeyboardShortcut keyboardShortcut = KeyboardShortcut.fromString("control S");
        String[] actions = keymap.getActionIds(keyboardShortcut);
        assertTrue(Arrays.equals(new String[]{"Replace"}, actions));
    }
}
