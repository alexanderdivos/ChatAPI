package de.mickare.chatapi;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.mickare.chatapi.api.IChatBBCodeBuilder;
import de.mickare.chatapi.api.IChatBuilder;
import de.mickare.chatapi.api.IChatMessageFactory;
import de.mickare.chatapi.api.IComponentChat;
import de.mickare.chatapi.api.action.ActionClick;
import de.mickare.chatapi.api.action.ActionHover;
import de.mickare.chatapi.chat.ComponentText;
import de.mickare.chatapi.chat.event.ClickEvent;
import de.mickare.chatapi.chat.event.HoverEvent;

public class BBCodeChatBuilder<P> extends ChatBuilder<P> implements IChatBBCodeBuilder<P> {
	// , Pattern.CASE_INSENSITIVE & Pattern.DOTALL )
	private static final String PATTERN_BOLD = "\\[(b)\\]|\\[(\\/b)\\]";
	private static final String PATTERN_ITALIC = "\\[(i)\\]|\\[(\\/i)\\]";
	private static final String PATTERN_UNDERLINE = "\\[(u)\\]|\\[(\\/u)\\]";
	private static final String PATTERN_STRIKETHROUGH = "\\[(s)\\]|\\[(\\/s)\\]";
	private static final String PATTERN_OBFUSCATED = "\\[(o)\\]|\\[(\\/o)\\]";
	private static final String PATTERN_RESETTED = "\\[(r)\\]|\\[(\\/r)\\]";

	private static final String PATTERN_PLAIN = "\\[(plain)\\]|\\[(\\/plain)\\]";

	private static final String PATTERN_COLOR = "\\[(c)=([0-9a-fk-or])\\]|\\[(\\/c)\\]";

	private static final String PATTERN_CLICK = "\\[(click)=([a-zA-Z_]+)\\([\\s]*[\\\"]([^\\\"\\\\]*(\\\\.[^\\\"\\\\]*)*)[\\\"][\\s]*\\)\\]|\\[(\\/click)\\]";
	private static final String PATTERN_HOVER = "\\[(hover)=([a-zA-Z_]+)\\([\\s]*[\\\"]([^\\\"\\\\]*(\\\\.[^\\\"\\\\]*)*)[\\\"][\\s]*\\)\\]|\\[(\\/hover)\\]";

	private static final Pattern PATTERN_BBCODE;

	private boolean plain = false;

	private static final String buildPatternBBCode() {
		StringBuilder sb = new StringBuilder();
		sb.append( PATTERN_BOLD ).append( "|" );
		sb.append( PATTERN_ITALIC ).append( "|" );
		sb.append( PATTERN_UNDERLINE ).append( "|" );
		sb.append( PATTERN_STRIKETHROUGH ).append( "|" );
		sb.append( PATTERN_OBFUSCATED ).append( "|" );
		sb.append( PATTERN_RESETTED ).append( "|" );
		sb.append( PATTERN_COLOR ).append( "|" );
		sb.append( PATTERN_CLICK ).append( "|" );
		sb.append( PATTERN_HOVER ).append( "|" );
		sb.append( PATTERN_PLAIN );
		return sb.toString();
	}

	static {
		PATTERN_BBCODE = Pattern.compile( buildPatternBBCode(), Pattern.CASE_INSENSITIVE & Pattern.DOTALL );
	}

	public BBCodeChatBuilder(IChatMessageFactory<P> messageFactory) {
		super( messageFactory );
	}

	@Override
	public IChatBBCodeBuilder<P> appendBBCode( final String message ) {
		if (message == null || message.isEmpty()) {
			return this;
		}

		Matcher matcher = PATTERN_BBCODE.matcher( message );
		int last_match = 0;
		String match = null;

		while (matcher.find()) {
			int groupId = 0;
			while ((match = matcher.group( ++groupId )) == null) {
				// NOOP
			}
			match = match.toLowerCase();

			if (last_match > matcher.start()) {
				continue;
			}

			if (this.plain) {
				// Only handle /plain elements
				if (match.equals( "/plain" )) {
					this.plain = false;
					if (last_match < matcher.start()) {
						this.appendText( message.substring( last_match, matcher.start() ) );
					}
				}

			} else {
				// Handle all elements
				
				if (last_match < matcher.start()) {
					this.appendText( message.substring( last_match, matcher.start() ) );
				}

				last_match = matcher.end();
				if (match.equals( "plain" )) {
					this.plain = true;
				} else if (match.equals( "/plain" )) {
					// NOOP
				} else if (match.equals( "b" )) {
					this.openColor( ChatColor.BOLD );
				} else if (match.equals( "i" )) {
					this.openColor( ChatColor.ITALIC );
				} else if (match.equals( "u" )) {
					this.openColor( ChatColor.UNDERLINE );
				} else if (match.equals( "s" )) {
					this.openColor( ChatColor.STRIKETHROUGH );
				} else if (match.equals( "o" )) {
					this.openColor( ChatColor.MAGIC );
				} else if (match.equals( "r" )) {
					this.openColor( ChatColor.RESET );
				} else if (match.equals( "c" )) {
					ChatColor color = ChatColor.getByChar( matcher.group( groupId + 1 ).charAt( 0 ) );
					this.openColor( color );
				} else if (match.equals( "/b" )) {
					this.closeColor( ChatColor.BOLD );
				} else if (match.equals( "/i" )) {
					this.closeColor( ChatColor.ITALIC );
				} else if (match.equals( "/u" )) {
					this.closeColor( ChatColor.UNDERLINE );
				} else if (match.equals( "/s" )) {
					this.closeColor( ChatColor.STRIKETHROUGH );
				} else if (match.equals( "/o" )) {
					this.closeColor( ChatColor.MAGIC );
				} else if (match.equals( "/r" )) {
					this.closeColor( ChatColor.RESET );
				} else if (match.equals( "/c" )) {
					this.closeColor();
				} else if (match.equals( "click" )) {

					// Logger.getLogger( "ChatAPI" ).info( "group1: " + matcher.group( groupId + 1 ) );
					// Logger.getLogger( "ChatAPI" ).info( "group2: " + matcher.group( groupId + 2 ) );

					final ActionClick action = ActionClick.fromString( matcher.group( groupId + 1 ).toLowerCase() );
					final String value = matcher.group( groupId + 2 );
					final ClickEvent event = new ClickEvent( action, value );
					this.openClick( event );
				} else if (match.equals( "/click" )) {
					this.closeClick();
				} else if (match.equals( "hover" )) {

					// Logger.getLogger( "ChatAPI" ).info( "group1: " + matcher.group( groupId + 1 ) );
					// Logger.getLogger( "ChatAPI" ).info( "group2: " + matcher.group( groupId + 2 ) );

					final ActionHover action = ActionHover.fromString( matcher.group( groupId + 1 ).toLowerCase() );
					final String value = matcher.group( groupId + 2 );
					IComponentChat cvalue = null;
					if (action.equals( ActionHover.SHOW_TEXT )) {
						final IChatBuilder<P> cb = this.getMessageFactory().newBBCodeBuilder().setOneLiner( true ).appendText( value );
						final List<IComponentChat> l = cb.getComponents();
						if (l.isEmpty() || l.get( 0 ) == null) {
							cvalue = ComponentText.create( value );
						} else {
							cvalue = l.get( 0 );
						}
					} else {
						cvalue = ComponentText.create( value );
					}
					Verify.checkNotNull( cvalue );
					final HoverEvent event = new HoverEvent( action, cvalue );
					this.openHover( event );
				} else if (match.equals( "/hover" )) {
					this.closeHover();
				}
				// if( this.plain == false ) - END
			}
			// while( matcher.find() ) - END
		}

		if (last_match < message.length()) {
			this.appendText( message.substring( last_match, message.length() ) );
		}

		return this;
	}

	@Override
	public boolean isPlain() {
		return plain;
	}

	@Override
	public IChatBBCodeBuilder<P> setPlain( boolean plain ) {
		this.plain = plain;
		return this;
	}

	
	
}
