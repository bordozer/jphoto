package ui.dtos;

import java.util.List;

public class AnonymousSettingsDTO {

	private final boolean forcedAnonymousPosting;
	private final List<String> messages;

	public AnonymousSettingsDTO( final boolean forcedAnonymousPosting, final List<String> messages ) {
		this.forcedAnonymousPosting = forcedAnonymousPosting;
		this.messages = messages;
	}

	public boolean isForcedAnonymousPosting() {
		return forcedAnonymousPosting;
	}

	public List<String> getMessages() {
		return messages;
	}
}
