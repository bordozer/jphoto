package ui.controllers.photos.groupoperations.handlers;

import core.general.photo.Photo;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.photoTeam.PhotoTeam;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.services.system.Services;
import core.services.user.UserTeamService;
import core.services.utils.EntityLinkUtilsService;
import ui.controllers.photos.groupoperations.GroupOperationResult;
import ui.controllers.photos.groupoperations.PhotoGroupOperationEntry;
import ui.controllers.photos.groupoperations.PhotoGroupOperationEntryProperty;
import ui.controllers.photos.groupoperations.PhotoGroupOperationModel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ArrangeTeamMembersHandler extends AbstractGroupOperationHandler {

	public ArrangeTeamMembersHandler( final PhotoGroupOperationModel model, final Services services ) {
		super( model, services );
	}

	@Override
	public PhotoGroupOperationType getPhotoGroupOperation() {
		return PhotoGroupOperationType.ARRANGE_TEAM_MEMBERS;
	}

	@Override
	public void fillModel() {
		super.fillModel();

		model.setUserTeamMembers( services.getUserTeamService().loadAllForEntry( getUserId() ) );
	}

	@Override
	protected void setGroupOperationEntryProperties( final PhotoGroupOperationEntry groupOperationEntry ) {
		final UserTeamService userTeamService = services.getUserTeamService();

		final Photo photo = groupOperationEntry.getPhoto();

		final Map<String, PhotoGroupOperationEntryProperty> map = model.getPhotoGroupOperationEntryPropertiesMap();
		final UserTeam userTeam = userTeamService.loadUserTeam( photo.getUserId() );

		for ( final UserTeamMember teamMember : userTeam.getUserTeamMembers() ) {
			final int teamMemberId = teamMember.getId();
			final String translate = getTranslatorService().translate( teamMember.getTeamMemberNameWithType( getTranslatorService(), getLanguage() ), getLanguage() );
			final String name = String.format( "<label for=\"photoGroupOperationEntryPropertiesMap['%d_%d'].value\"><span class='label-%d'>%s</span></label>", photo.getId(), teamMemberId, teamMemberId, translate );

			final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty( photo.getId(), teamMemberId, name );
			entryProperty.setValue( userTeamService.isTeamMemberAssignedToPhoto( photo.getId(), teamMemberId ) );

			final String key = String.format( "%d_%d", photo.getId(), teamMemberId );
			map.put( key, entryProperty );
		}

		model.setPhotoGroupOperationEntryPropertiesMap( map );
	}

	@Override
	protected List<GroupOperationResult> performPhotoOperation( final Photo photo, final PhotoGroupOperationEntryProperty entryProperty ) {

		final List<GroupOperationResult> operationResults = newArrayList();

		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
		final UserTeamService userTeamService = services.getUserTeamService();

		final PhotoTeam existingPhotoTeam = userTeamService.getPhotoTeam( photo.getId() );

		final List<PhotoTeamMember> newPhotoTeamMembers = newArrayList( existingPhotoTeam.getPhotoTeamMembers() );

		final int teamMemberId = entryProperty.getEntryId();

		final UserTeamMember teamMember = userTeamService.load( teamMemberId );

		boolean isTeamMemberChecked = entryProperty.isValue();
		boolean isTeamMemberAssignedToPhoto = isTeamMemberAlreadyAssignedToPhoto( teamMember, newPhotoTeamMembers );

		if ( isTeamMemberChecked && !isTeamMemberAssignedToPhoto ) {
			final PhotoTeamMember photoTeamMember = new PhotoTeamMember();
			photoTeamMember.setUserTeamMember( teamMember );

			newPhotoTeamMembers.add( photoTeamMember );

			userTeamService.savePhotoTeam( new PhotoTeam( photo, newPhotoTeamMembers ) );
			operationResults.add( GroupOperationResult.successful( String.format( "%s has been assigned to photo '%s'", entityLinkUtilsService.getUserTeamMemberCardLink( teamMember, getLanguage() ), entityLinkUtilsService.getPhotoCardLink( photo, getLanguage() ) ) ) );
		}

		if ( ! isTeamMemberChecked && isTeamMemberAssignedToPhoto ) {
			removeTeamMember( teamMember, newPhotoTeamMembers );

			userTeamService.savePhotoTeam( new PhotoTeam( photo, newPhotoTeamMembers ) );

			operationResults.add( GroupOperationResult.successful( String.format( "%s has been removed from photo '%s'", entityLinkUtilsService.getUserTeamMemberCardLink( teamMember, getLanguage() ), entityLinkUtilsService.getPhotoCardLink( photo, getLanguage() ) ) ) );
		}

		return operationResults;
	}

	private boolean isTeamMemberAlreadyAssignedToPhoto( final UserTeamMember beingAddedTeamMember, final List<PhotoTeamMember> teamMembers ) {
		for ( final PhotoTeamMember existingTeamMember : teamMembers ) {
			if ( existingTeamMember.getUserTeamMember().equals( beingAddedTeamMember ) ) {
				return true;
			}
		}

		return false;
	}

	private void removeTeamMember( final UserTeamMember teamMemberToRemove, final List<PhotoTeamMember> teamMembers ) {
		final Iterator<PhotoTeamMember> iterator = teamMembers.iterator();
		while ( iterator.hasNext() ) {
			final PhotoTeamMember member = iterator.next();
			if ( member.getUserTeamMember().equals( teamMemberToRemove ) ) {
				iterator.remove();
				break;
			}
		}
	}
}
