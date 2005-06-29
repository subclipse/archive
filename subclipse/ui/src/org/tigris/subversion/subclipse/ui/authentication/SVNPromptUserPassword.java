package org.tigris.subversion.subclipse.ui.authentication;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.tigris.subversion.subclipse.ui.Policy;
import org.tigris.subversion.subclipse.ui.SVNUIPlugin;
import org.tigris.subversion.svnclientadapter.ISVNPromptUserPassword;

public class SVNPromptUserPassword implements ISVNPromptUserPassword {
    private String username;
    private String password;
    private boolean allowedSave;
    private String realm;
    private boolean maySave;
    private boolean rtnCode;
    private int trust;
    private String info;
    private boolean allowPermanently;
    private boolean yesNoAnswer;
    private String question;
    private boolean yesIsDefault;
    private String answer;
    private boolean showAnswer;

    public SVNPromptUserPassword() {
        super();
    }

    public boolean askYesNo(String realm, String askQuestion, boolean askYesIsDefault) {
		question = askQuestion;
		yesIsDefault = askYesIsDefault;
        SVNUIPlugin.getStandardDisplay().syncExec(new Runnable() {
			public void run() {
		        int defaultButton = 0;
		        if (!yesIsDefault) defaultButton = 1;
		        MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
		                Policy.bind("SVNPromptUserPassword.authentication"), //$NON-NLS-1$
		                null,
		                question,
		                MessageDialog.QUESTION,
		                new String[] {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL},
		                defaultButton);
		        yesNoAnswer = (dialog.open() == 0);				
			}
		});        
		return yesNoAnswer;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int askTrustSSLServer(String trustInfo, boolean trustAllowPermanently) {
        info = trustInfo;
        allowPermanently = trustAllowPermanently;
		SVNUIPlugin.getStandardDisplay().syncExec(new Runnable() {
			public void run() {
		        TrustSSLServerDialog dialog = new TrustSSLServerDialog(Display.getCurrent().getActiveShell(),
		                info, allowPermanently);
		        switch (dialog.open()) {
		        case TrustSSLServerDialog.REJECT:
		            trust =  ISVNPromptUserPassword.Reject;
		        	break;
		        case TrustSSLServerDialog.TEMPORARY:
		            trust = ISVNPromptUserPassword.AcceptTemporary;
		        	break;
		        case TrustSSLServerDialog.PERMANENT:
		            trust = ISVNPromptUserPassword.AcceptPermanently;
		        	break;
		        default:
		            trust = TrustSSLServerDialog.REJECT;
		        }				
			}
		});        
        return trust;
    }

    public boolean prompt(String promptRealm, String promptUsername, boolean promptMaySave) {
        rtnCode = false;
        username = promptUsername;
        realm = promptRealm;
        maySave = promptMaySave;
   		SVNUIPlugin.getStandardDisplay().syncExec(new Runnable() {
			public void run() {
		        PasswordPromptDialog dialog = new PasswordPromptDialog(SVNUIPlugin.getStandardDisplay().getActiveShell(),
		                realm, username, maySave);
		        if (dialog.open() == PasswordPromptDialog.OK) {
		            username = dialog.getUsername();
		            password = dialog.getPassword();
		            allowedSave = dialog.isSave();
		            rtnCode = true;
		        }				
			}
		});      
        return rtnCode;
    }

    public String askQuestion(String askRealm, String askQuestion, boolean askShowAnswer,
            boolean askMaySave) {
        answer = "";
        realm = askRealm;
        maySave = askMaySave;
        showAnswer = askShowAnswer;
		SVNUIPlugin.getStandardDisplay().syncExec(new Runnable() {
			public void run() {
		        QuestionDialog dialog = new QuestionDialog(Display.getCurrent().getActiveShell(),
		                realm, question, showAnswer, maySave);
		        if (dialog.open() == QuestionDialog.OK) {
		            allowedSave = dialog.isSave();
		            answer = dialog.getAnswer();
		        }				
			}
		});        
        return answer;
    }

    public boolean userAllowedSave() {
        return allowedSave;
    }

}
