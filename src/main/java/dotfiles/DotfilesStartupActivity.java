package dotfiles;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class DotfilesStartupActivity implements StartupActivity {
    private static final Logger LOG = Logger.getInstance(DotfilesStartupActivity.class);

    @Override
    public void runActivity(@NotNull Project project) {
        LOG.info("StartupActivity started");

        LocalFileSystem fileSystem = LocalFileSystem.getInstance();

        VirtualFile dotfilesDirectory = fileSystem.findFileByPath(Dotfile.getDirectory());
        dotfilesDirectory.getChildren();
        fileSystem.addRootToWatch(Dotfile.getDirectory(), true);

        for (Dotfile dotfile : Dotfile.getDotfiles()) {
            dotfile.apply();
        }
    }
}
