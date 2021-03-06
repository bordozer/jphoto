package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;

public class PhotoAdminMovePhotoToGenreSubMenuItem extends AbstractPhotoMenuItemOperationAdmin {

    private final Genre genre;

    public PhotoAdminMovePhotoToGenreSubMenuItem(final Photo photo, final User accessor, final Genre genre, final Services services) {
        super(photo, accessor, services);

        this.genre = genre;
    }

    @Override
    protected boolean isSystemConfigurationKeyIsOn() {
        return true;
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ITEM;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                final String genreToMoveName = getTranslatorService().translateGenre(genre, getLanguage());

                if (isPhotoGenre()) {
                    return getTranslatorService().translate("PhotoMenuItem: $1 - current photo category", getLanguage(), genreToMoveName);
                }

                final TranslatableMessage translatableMessage = new TranslatableMessage("", services).string(genreToMoveName);

                if (genre.isContainsNudeContent() && !menuEntry.isContainsNudeContent()) {
                    translatableMessage.worldSeparator().translatableString("PhotoMenuItem: ( + NUDE content will be set after photo moving to the new genre)");
                }

                if (!genre.isCanContainNudeContent() && menuEntry.isContainsNudeContent()) {
                    translatableMessage.worldSeparator().translatableString("PhotoMenuItem: ( - NUDE content will be removed after photo moving to the new genre)");
                }

                return translatableMessage.build(getLanguage());
            }

            @Override
            public String getMenuCommand() {
                if (isPhotoGenre()) {
                    return "return false;";
                }

                return String.format("movePhotoToCategory( %d, %d, %s );", getId(), genre.getId(), RELOAD_PHOTO_CALLBACK);
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {
        return isAccessorSuperAdmin();
    }

    @Override
    public String getMenuCssClass() {
        return isPhotoGenre() ? MENU_ITEM_DISABLED_CSS_CLASS : MENU_ITEM_ADMIN_CSS_CLASS;
    }

    private boolean isPhotoGenre() {
        return menuEntry.getGenreId() == genre.getId();
    }

    @Override
    public String getCallbackMessage() {
        final TranslatableMessage translatableMessage = new TranslatableMessage("PhotoMenuItem: photo $1: has been moved to genre '$2'", services)
                .addPhotoCardLinkParameter(menuEntry.getId())
                .addPhotosByGenreLinkParameter(genre);

        if (genre.isContainsNudeContent() && !menuEntry.isContainsNudeContent()) {
            translatableMessage
                    .lineBreakHtml()
                    .translatableString("PhotoMenuItem: Nude content has been set after the photo moving to the new category")
            ;
        }

        if (!genre.isCanContainNudeContent() && menuEntry.isContainsNudeContent()) {
            translatableMessage
                    .lineBreakHtml()
                    .translatableString("PhotoMenuItem: Nude content has been removed after the photo moving to the new category")
            ;
        }

        return translatableMessage.build(getLanguage());
        //		return services.getTranslatorService().translate( "PhotoMenuItem: photo $1: has been moved to genre '$2'", getLanguage(), menuEntry.getNameEscaped(), getGenreNameTranslated( genre) );
    }
}
