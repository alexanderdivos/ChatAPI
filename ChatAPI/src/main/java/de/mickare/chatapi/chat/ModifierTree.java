package de.mickare.chatapi.chat;

import java.util.LinkedList;
import java.util.logging.Logger;

import de.mickare.chatapi.ChatColor;
import de.mickare.chatapi.Verify;
import de.mickare.chatapi.api.IComponentChat;
import de.mickare.chatapi.api.IEventClick;
import de.mickare.chatapi.api.IEventHover;

public class ModifierTree {

	private final ModifierTree parent;
	private final Object value;

	private IComponentChat component = null;

	private final LinkedList<ChatColor> colorElements = new LinkedList<>();

	public ModifierTree() {
		this( null, null );
	}

	public ModifierTree(final ModifierTree parent, final Object value) {
		this.parent = parent;
		this.value = value;
	}

	public final Object getValue() {
		return value;
	}

	public final ModifierTree getParent() {
		return parent;
	}

	public final ModifierTree close() {
		return parent != null ? parent : this;
	}

	private final ModifierTree _close( final Object o ) {
		Verify.checkNotNull( o );
		Logger.getLogger( "ChatAPI" ).info( o.toString() );
		if (this.value == o) {
			return close();
		}
		return parent != null ? parent._close( o ) : this;
	}

	private final ModifierTree _closeClass( final Class<?> c ) {
		Verify.checkNotNull( c );
		Logger.getLogger( "ChatAPI" ).info( "Close class: " + c.getName()  );
		if (!c.isInstance( this.value )) {
			return parent != null ? parent._closeClass( c ) : this;
		}
		return close();
	}

	private final ModifierTree _open( final Object value ) {
		Verify.checkNotNull( value );
		return new ModifierTree( this, value );
	}

	public final void add( final ChatColor color ) {
		Verify.checkNotNull( color );
		this.colorElements.add( color );
	}

	public final ModifierTree open( final ChatColor color ) {
		return _open( color );
	}

	public final ModifierTree closeColor() {
		return _closeClass( ChatColor.class );
	}

	public final ModifierTree close( final ChatColor color ) {
		Logger.getLogger( "ChatAPI" ).info( "Close color" + color.toString() );
		return _close( color );
	}

	public final ModifierTree open( final IEventClick event ) {
		return _open( event );
	}

	public final ModifierTree closeClick() {
		return _closeClass( IEventClick.class );
	}

	public final ModifierTree close( final IEventClick event ) {
		Logger.getLogger( "ChatAPI" ).info( "Close IEventClick" + event.toString() );
		return _close( event );
	}

	public final ModifierTree open( final IEventHover event ) {
		return _open( event );
	}

	public final ModifierTree closeHover() {
		return _closeClass( IEventHover.class );
	}

	public final ModifierTree close( final IEventHover event ) {
		Logger.getLogger( "ChatAPI" ).info( "Close IEventHover" + event.toString() );
		return _close( event );
	}

	public final ChatModifierDifference getFormatDifference( ModifierTree targetParent ) {
		return getFormatDifference( targetParent, new ChatModifierDifference() );
	}

	private final ChatModifierDifference getFormatDifference( final ModifierTree targetParent, final ChatModifierDifference out ) {
		if (this == targetParent) {
			for (final ChatColor c : this.colorElements) {
				out.setColorClassic( c );
			}
			return out;
		}
		if (parent != null) {
			parent.getFormatDifference( targetParent, out );
		}
		if (this.value != null) {
			if (this.value instanceof ChatColor) {
				out.setColor( (ChatColor) this.value );
			} else if (this.value instanceof IEventClick) {
				out.setClickEvent( (IEventClick) this.value );
			} else if (this.value instanceof IEventHover) {
				out.setHoverEvent( (IEventHover) this.value );
			} else {
				throw new IllegalStateException();
			}

		}
		for (final ChatColor c : this.colorElements) {
			out.setColorClassic( c );
		}

		return out;
	}

	public final void clearComponents() {
		this.clearComponents( null );
	}

	public final void clearComponents( final IComponentChat rootComp ) {
		if (parent != null) {
			parent.clearComponents( rootComp );
			this.component = null;
		} else {
			this.component = rootComp;
		}
	}

	public ModifierTree getActiveNode() {
		if (this.component != null) {
			return this;
		}
		if (parent != null) {
			return parent.getActiveNode();
		}
		return this;
	}

	public IComponentChat getComponent() {
		return this.component;
	}

	public IComponentChat addComponent( final IComponentChat c ) {
		ModifierTree active = this.getActiveNode();
		IComponentChat clone = this.getFormatDifference( active ).format( active.getComponent().add( c ) );
		if (this.component == null) {
			this.component = clone;
		}
		return clone;
	}

}
