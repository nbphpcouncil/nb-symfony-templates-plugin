/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbphpcouncil.modules.php.symfony2.filetemplates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.nbphpcouncil.modules.php.symfony2.filetemplates.utils.FileTemplatesUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

// TODO define position attribute
//@TemplateRegistration(folder = "AWTForms", displayName = "#Symfony2EntityWizardIterator_displayName", description = "symfony2Entity.html")
//@Messages("Symfony2EntityWizardIterator_displayName=Symfony2 Entity")
public final class Symfony2EntityWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private int index;
    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;
    private static final Logger LOGGER = Logger.getLogger(Symfony2EntityWizardIterator.class.getName());

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        Project project = Templates.getProject(wizard);
        Sources sources = ProjectUtils.getSources(project);
        SourceGroup[] groups = sources.getSourceGroups("PHPSOURCE");
        Templates.SimpleTargetChooserBuilder chooserBuilder = Templates.buildSimpleTargetChooser(project, groups);
        chooserBuilder.bottomPanel(new Symfony2EntityWizardPanel1());

        if (panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
            panels.add(chooserBuilder.create());
        }

        return panels;
    }

    @Override
    public Set<FileObject> instantiate() throws IOException {
        boolean isRepositoryDeclared = (Boolean) wizard.getProperty(Symfony2EntityVisualPanel1.IS_REPO_DECLARED);
        String optionalTableName = (String) wizard.getProperty(Symfony2EntityVisualPanel1.OPTIONAL_TABLE_NAME);

        //Get the package:
        FileObject entityDir = Templates.getTargetFolder(wizard);
        DataFolder df = DataFolder.findFolder(entityDir);

        //Get the template and convert it:
        FileObject template = Templates.getTemplate(wizard);
        DataObject dTemplate = DataObject.find(template);

        //Get the class:
        String targetName = Templates.getTargetName(wizard);

        Map<String, Object> args = new HashMap<String, Object>();

        if (FileTemplatesUtils.isNullOrEmpty(optionalTableName)) {
            optionalTableName = FileTemplatesUtils.tableize(targetName);
        }

        String namespace = FileTemplatesUtils.getNamespaceForPhp(entityDir.toURL().toString());

        args.put("repoDeclared", isRepositoryDeclared);
        args.put("optionalTableName", optionalTableName);
        args.put("namespaceDir", namespace);

        Set<FileObject> files = new HashSet<>();
        if (isRepositoryDeclared) {

            String repoNamespace = FileTemplatesUtils.getNamespaceForRepository(namespace);

            args.put("namespaceForRepository", repoNamespace);

            Map<String, Object> repoArgs = new HashMap<>();
            repoArgs.put("repoNamespace", repoNamespace);

            FileObject repositoryTemplate = FileUtil.getConfigFile("Templates/Scripting/Symfony2RepositoryTemplate.php"); // NOI18N
            assert repositoryTemplate != null;

            DataFolder repositoryDataFolder = getRepositoryDataFolder(entityDir);
            if (repositoryDataFolder != null) {
                DataObject repositoryDataObject = DataObject.find(repositoryTemplate);
                String repositoryFileName = String.format("%sRepository", targetName); // NOI18N
                DataObject createdRepository = repositoryDataObject.createFromTemplate(repositoryDataFolder, repositoryFileName, repoArgs);
                if (createdRepository != null) {
                    FileObject repositoryFile = createdRepository.getPrimaryFile();
                    if (repositoryFile != null) {
                        files.add(repositoryFile);
                    }
                }
            } else {
                LOGGER.log(Level.INFO, "Can't find the Repository directoy.");
            }
        }

        //Define the template from the above,
        //passing the package, the file name, and the map of strings to the template:
        DataObject dobj = dTemplate.createFromTemplate(df, targetName, args);

        //Obtain a FileObject:
        FileObject createdFile = dobj.getPrimaryFile();
        if (createdFile != null) {
            files.add(createdFile);
        }
        //Create the new file:
        return files;
    }

    private DataFolder getRepositoryDataFolder(FileObject entityDir) {
        DataFolder repositoryDataFolder = null;
        FileObject parent = entityDir.getParent();
        if (parent != null) {
            FileObject repositoryDir = parent.getFileObject("Repository"); // NOI18N
            if (repositoryDir != null && repositoryDir.isFolder()) {
                repositoryDataFolder = DataFolder.findFolder(repositoryDir);
            }
        }
        return repositoryDataFolder;
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
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

}
