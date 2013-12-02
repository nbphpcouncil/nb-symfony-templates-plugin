/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.udevi.symfony2.filetemplates;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

/**
 *
 * @author tpoperecinii
 */
public class Symfony2ControllerWizardIterator extends Symfony2BaseWizardIterator {
    
    private WizardDescriptor wizard;
    
    @Override
    public Set<FileObject> instantiate() throws IOException {
        wizard = super.getWizard();
        FileObject dir = Templates.getTargetFolder(wizard);
        FileObject template = Templates.getTemplate(wizard);
        
        Map<String, String> wizardArgs = getArgsWithNamespace(dir);
        
        DataFolder dataFolder = DataFolder.findFolder(dir);
        DataObject dataTemplate = DataObject.find(template);
        DataObject createdFile = dataTemplate.createFromTemplate(dataFolder, 
                Templates.getTargetName(wizard), wizardArgs);
        
        return Collections.singleton(createdFile.getPrimaryFile());
    }
    
}
