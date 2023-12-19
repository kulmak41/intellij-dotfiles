package dotfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontCache;
import com.intellij.openapi.editor.colors.FontPreferences;
import com.intellij.openapi.editor.colors.impl.AppEditorFontOptions;
import com.intellij.openapi.editor.colors.impl.EditorColorsManagerImpl;

import java.io.File;
import java.io.IOException;

public class AppearanceDotfile extends Dotfile {
    private static final AppearanceDotfile instance = new AppearanceDotfile();

    private AppearanceDotfile() {
    }

    public static AppearanceDotfile getInstance() {
        return instance;
    }

    private static final Logger LOG = Logger.getInstance(DotfilesStartupActivity.class);

    private static class Appearance {
        private String fontName;
        private Float fontSize;
        private String theme;

        public String getFontName() {
            return fontName;
        }

        public Float getFontSize() {
            return fontSize;
        }

        public String getTheme() {
            return theme;
        }
    }

    @Override
    public String getPath() {
        return directory + File.separator + "appearance.json";
    }

    @Override
    public void apply() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(getPath());
        try {
            Appearance appearance = mapper.readValue(file, Appearance.class);
            if (appearance.getTheme() != null) {
                LafManager lafManager = LafManager.getInstance();
                var themes = lafManager.getInstalledLookAndFeels();
                for (var theme : themes) {
                    if (theme.getName().equals(appearance.getTheme())) {
                        lafManager.setCurrentLookAndFeel(theme);
                        lafManager.updateUI();
                        break;
                    }
                }
            }

            EditorColorsScheme scheme = (EditorColorsScheme) EditorColorsManager.getInstance().getGlobalScheme().clone();
            if (appearance.getFontName() != null) {
                scheme.setEditorFontName(appearance.getFontName());
            }
            if (appearance.getFontSize() != null) {
                scheme.setEditorFontSize(appearance.getFontSize());
            }
            FontPreferences preferences = scheme.getFontPreferences();
            AppEditorFontOptions.getInstance().update(preferences);
            EditorFontCache.getInstance().reset();
            ((EditorColorsManagerImpl) EditorColorsManager.getInstance()).schemeChangedOrSwitched(null);
            EditorFactory.getInstance().refreshAllEditors();

            LOG.info("Settings from the appearance dotfile have been applied successfully");
        } catch (IOException e) {
            LOG.info("An error occurred in the process of parsing settings from the appearance dotfile");
        }
    }
}
