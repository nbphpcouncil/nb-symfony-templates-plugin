/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbphpcouncil.modules.php.symfony2.filetemplates;

import java.awt.Component;
import java.io.IOException;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.nbphpcouncil.modules.php.symfony2.filetemplates.utils.FileTemplatesUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

// TODO define position attribute
public class Symfony2BaseWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {
    public static final String NAMESPACE_DIR = "namespaceDir";
    
    private int index;
    private WizardDescriptor wizard;

    
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

    public Map<String, String> getArgsWithNamespace(FileObject dir) throws IOException {
        
        String namespace = FileTemplatesUtils.getNamespaceForPhp(dir.toURL().toString());
        Map<String, String> wizardArgs = Collections.singletonMap(NAMESPACE_DIR, namespace);
        
        return wizardArgs;
    }

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        Project project = Templates.getProject(wizard);
        Sources sources = ProjectUtils.getSources(project);
        SourceGroup[] groups = sources.getSourceGroups("PHPSOURCE");
        
        WizardDescriptor.Panel<WizardDescriptor> simpleTargetChooserPanel = Templates.buildSimpleTargetChooser(project, groups).freeFileExtension().create();

        panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(simpleTargetChooserPanel);
        return panels;
    }

    @Override
    public Set<FileObject> instantiate() throws IOException {
        FileObject dir = Templates.getTargetFolder(wizard);
        FileObject template = Templates.getTemplate(wizard);
        
        Map<String, String> wizardArgs = getArgsWithNamespace(dir);
        
        DataFolder dataFolder = DataFolder.findFolder(dir);
        DataObject dataTemplate = DataObject.find(template);
        DataObject createdFile = dataTemplate.createFromTemplate(dataFolder, 
                Templates.getTargetName(wizard), wizardArgs);
        
        return Collections.singleton(createdFile.getPrimaryFile());
    }
    
    

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        panels = getPanels();

        // Make sure list of steps is accurate.
        String[] beforeSteps = (String[]) wizard.getProperty(WizardDescriptor.PROP_CONTENT_DATA);
        int beforeStepLength = beforeSteps.length - 1;
        String[] steps = createSteps(beforeSteps);
        
        int panelSize = panels.size();
        
        for (int i = 0; i < panelSize; i++) {
            Component c = panels.get(i).getComponent();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(i + beforeStepLength - 1)); // NOI18N
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps); // NOI18N
            }
        }
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return panels.get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + panels.size();
    }

    @Override
    public boolean hasNext() {
        return index < panels.size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
   private String[] createSteps(String[] beforeSteps) {
        int beforeStepLength = beforeSteps.length - 1;
        String[] res = new String[beforeStepLength + panels.size()];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeStepLength)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels.get(i - beforeStepLength).getComponent().getName();
            }
        }
        return res;
    }
   
    public WizardDescriptor getWizard() {
        return wizard;
    }
}
