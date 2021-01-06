package io.github.kings1990.serializable;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;

public class SerializableWithIdAction extends AnAction {

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

        if(!text.contains(" Serializable") || !text.contains("serialVersionUID") ){
            Runnable runnable = () -> {
                if(!text.contains("serialVersionUID")) {
                    document.insertString(offset + 1, "\n\tprivate static final long serialVersionUID = 1L;");
                }
                if(!text.contains(" Serializable")) {
                    document.insertString(offset - 1, " implements Serializable");
                    document.insertString(offsetPackage + 1, "\nimport java.io.Serializable;\n");
                }
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
        }
    }

    protected PsiClass getTargetClass(Editor editor, PsiFile psiFile) {
        VirtualFile virtualFile = psiFile.getVirtualFile();
        String nameWithoutExtension = virtualFile.getNameWithoutExtension();
        System.out.println(nameWithoutExtension);
        int offset = editor.getDocument().getText().indexOf(nameWithoutExtension);


        PsiElement element = psiFile.findElementAt(offset);
        if (element == null) {
            return null;
        } else {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            return target instanceof SyntheticElement ? null : target;
        }
    }
}
