import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import dotfiles.AppearanceDotfile;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class AppearanceDotfileTest extends BasePlatformTestCase {
    public void testApply() throws FileNotFoundException {
        AppearanceDotfile appearanceDotfile = AppearanceDotfile.getInstance();

        PrintWriter writer = new PrintWriter(appearanceDotfile.getPath());
        writer.print("""
                {
                	"fontName": "JetBrains Mono",
                	"fontSize": 9
                }""");
        writer.close();

        appearanceDotfile.apply();

        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        assertEquals("JetBrains Mono", scheme.getEditorFontName());
        assertEquals(9, scheme.getEditorFontSize());
    }
}


