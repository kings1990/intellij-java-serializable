package io.github.kings1990.serializable;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

public class SerializableAction  extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Document document = editor.getDocument();

        // Visibility
        e.getPresentation().setVisible(true);
        // Enable or disable
        e.getPresentation().setEnabled(true);
        //获取当前在操作的工程上下文
        Project project = e.getData(PlatformDataKeys.PROJECT);

        //获取当前操作的类文件
//        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);

        String text = document.getText();
        int offset = text.indexOf("{");
        int offsetPackage = text.indexOf(";");

        if(!text.contains("Serializable")){
            Runnable runnable = () -> {
                document.insertString(offset - 1, " implements Serializable ");
                document.insertString(offsetPackage +1, "\nimport java.io.Serializable;\n");
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
        }
    }
}
