package admin.controllers.upgrade;

import admin.services.services.UpgradeMonitor;
import admin.services.services.UpgradeService;
import admin.services.services.UpgradeState;
import admin.upgrade.entities.UpgradeTaskLogEntry;
import admin.upgrade.entities.UpgradeTaskResult;
import admin.upgrade.entities.UpgradeTaskToPerform;
import admin.upgrade.tasks.AbstractUpgradeTask;
import core.exceptions.UpgradeException;
import core.services.translator.TranslatorService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsAdminService;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "upgrade" )
public class UpgradeController {

	public static final String MODEL_NAME = "upgradeModel";

	private static final String VIEW = "/admin/upgrade/UpgradeTaskList";
	private static final String VIEW_PROGRESS = "/admin/upgrade/UpgradeProgress";

	private static final String UPGRADE_TASKS_XNL = "upgradeTasks.xml";

	private static final String ELEMENT_RELEASE = "release";
	private static final String ELEMENT_VERSION_NAME = "name";
	private static final String ELEMENT_TASK = "task";
	private static final String ELEMENT_TASK_NAME = "name";
	public static final String UPGRADE_XML_FILE_FORLDER = "../properties";

	@Autowired
	private UpgradeService upgradeService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@ModelAttribute( MODEL_NAME )
	public UpgradeModel prepareModel() {
		final UpgradeModel model = new UpgradeModel();

		final List<UpgradeTaskLogEntry> performedUpgradeTasks = upgradeService.getPerformedUpgradeTasks();
		model.setUpgradeTaskLogEntries( performedUpgradeTasks );
		final List<UpgradeTaskToPerform> upgradeTaskToPerform;
		try {
			upgradeTaskToPerform = getUpgradeTaskToPerform();

			CollectionUtils.filter( upgradeTaskToPerform, new Predicate<UpgradeTaskToPerform>() {
				@Override
				public boolean evaluate( final UpgradeTaskToPerform upgradeTaskToPerform ) {
					return ! isTaskPerformed( upgradeTaskToPerform, performedUpgradeTasks );
				}
			} );

			model.setUpgradeTasksToPerform( upgradeTaskToPerform );
		} catch ( Throwable e ) {
			model.getBindingResult().reject( translatorService.translate( "File read error", EnvironmentContext.getLanguage() )
				, translatorService.translate( "Can not read upgrade tasks from XML", EnvironmentContext.getLanguage() ) );
		}

		model.setUpgradeMonitor( upgradeService.getUpgradeMonitor() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showForm( final @ModelAttribute( MODEL_NAME ) UpgradeModel model ) {

		model.setPageTitleData( breadcrumbsAdminService.getUpgradeTasksListBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String postForm( final @ModelAttribute( MODEL_NAME ) UpgradeModel model ) {

		if ( upgradeService.getUpgradeMonitor().getUpgradeState() != UpgradeState.IN_PROGRESS ) {

			synchronized ( upgradeService.getUpgradeMonitor() ) {

				if ( upgradeService.getUpgradeMonitor().getUpgradeState() != UpgradeState.IN_PROGRESS ) {

					upgradeService.getUpgradeMonitor().setUpgradeState( UpgradeState.IN_PROGRESS );

					new Thread(){
						@Override
						public void run() {
							upgradeService.performUpgrade( model.getUpgradeTasksToPerform(), EnvironmentContext.getCurrentUser() );
						}
					}.start();
				}
			}
		}

		return "redirect:/admin/upgrade/progress/";
	}

	@RequestMapping( method = RequestMethod.GET, value = "/progress/" )
	public String showProgress( final @ModelAttribute( MODEL_NAME ) UpgradeModel model ) {

		final UpgradeMonitor upgradeMonitor = upgradeService.getUpgradeMonitor();
		final int totalUpgradeTasks = upgradeMonitor.getTotalUpgradeTasks();
		final int done = upgradeMonitor.getDoneUpgradeTasks();
		final int error = upgradeMonitor.getErrorUpgradeTasks();

		model.setPageTitleData( breadcrumbsAdminService.getUpgradeBreadcrumbs( upgradeMonitor.getUpgradeState(), done, totalUpgradeTasks, error ) );

		return VIEW_PROGRESS;
	}

	private boolean isTaskPerformed( final UpgradeTaskToPerform upgradeTaskToPerform, final List<UpgradeTaskLogEntry> performedUpgradeTasks ) {
		for ( final UpgradeTaskLogEntry performedUpgradeTask : performedUpgradeTasks ) {
			if ( performedUpgradeTask.getUpgradeTaskResult() == UpgradeTaskResult.SUCCESSFUL && performedUpgradeTask.getUpgradeTaskName().equals( upgradeTaskToPerform.getUpgradeTaskName() ) ) {
				return true;
			}
		}
		return false;
	}

	private List<UpgradeTaskToPerform> getUpgradeTaskToPerform() throws DocumentException, UpgradeException {
		final SAXReader reader = new SAXReader( false );
		final File upgradeXml = new File( UPGRADE_XML_FILE_FORLDER, UPGRADE_TASKS_XNL );
		final Document document = reader.read( upgradeXml );

		return readXmlData( document );
	}

	private List<UpgradeTaskToPerform> readXmlData( final Document document ) throws UpgradeException {
		final List<UpgradeTaskToPerform> result = newArrayList();

		final Iterator iteratorVersions = document.getRootElement().elementIterator( ELEMENT_RELEASE );
		while ( iteratorVersions.hasNext() ) {
			final Element versionElement = ( Element ) iteratorVersions.next();

			String version = "";
			final Attribute attr = versionElement.attribute( ELEMENT_VERSION_NAME );
			if ( attr != null ) {
				version = attr.getValue();
			}
			final Iterator iteratorTasks = versionElement.elementIterator( ELEMENT_TASK );
			while ( iteratorTasks.hasNext() ) {
				final Element taskElement = ( Element ) iteratorTasks.next();
				final Attribute taskAttr = taskElement.attribute( ELEMENT_TASK_NAME );
				if ( taskAttr != null ) {
					result.add( instantiateUpgradeTask( version, taskAttr.getValue() ) );
				}
			}
		}

		return result;
	}

	private UpgradeTaskToPerform instantiateUpgradeTask( final String version, final String aClass ) throws UpgradeException {

		final UpgradeTaskToPerform upgradeTaskToPerform;
		try {
			final Class taskClass = Class.forName( aClass );
			final AbstractUpgradeTask upgradeTask = ( AbstractUpgradeTask ) taskClass.newInstance();
			upgradeTaskToPerform = new UpgradeTaskToPerform( upgradeTask.getClass().getName(), version, upgradeTask );
		} catch ( Throwable e ) {
			throw new UpgradeException( String.format( "Class %s can not be created or instantiated", aClass ) );
		}

		return upgradeTaskToPerform;
	}
}
