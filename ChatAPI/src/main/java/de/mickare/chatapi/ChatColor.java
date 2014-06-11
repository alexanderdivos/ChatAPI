package de.mickare.chatapi;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
* Simplistic enumeration of all supported color values for chat.
*/
public enum ChatColor
{

    /**
* Represents black.
*/
    BLACK( '0', "black" ),
    /**
* Represents dark blue.
*/
    DARK_BLUE( '1', "dark_blue" ),
    /**
* Represents dark green.
*/
    DARK_GREEN( '2', "dark_green" ),
    /**
* Represents dark blue (aqua).
*/
    DARK_AQUA( '3', "dark_aqua" ),
    /**
* Represents dark red.
*/
    DARK_RED( '4', "dark_red" ),
    /**
* Represents dark purple.
*/
    DARK_PURPLE( '5', "dark_purple" ),
    /**
* Represents gold.
*/
    GOLD( '6', "gold" ),
    /**
* Represents gray.
*/
    GRAY( '7', "gray" ),
    /**
* Represents dark gray.
*/
    DARK_GRAY( '8', "dark_gray" ),
    /**
* Represents blue.
*/
    BLUE( '9', "blue" ),
    /**
* Represents green.
*/
    GREEN( 'a', "green" ),
    /**
* Represents aqua.
*/
    AQUA( 'b', "aqua" ),
    /**
* Represents red.
*/
    RED( 'c', "red" ),
    /**
* Represents light purple.
*/
    LIGHT_PURPLE( 'd', "light_purple" ),
    /**
* Represents yellow.
*/
    YELLOW( 'e', "yellow" ),
    /**
* Represents white.
*/
    WHITE( 'f', "white" ),
    /**
* Represents magical characters that change around randomly.
*/
	MAGIC('k', "obfuscated", true),
    /**
* Makes the text bold.
*/
    BOLD( 'l', "bold", true ),
    /**
* Makes a line appear through the text.
*/
    STRIKETHROUGH( 'm', "strikethrough", true ),
    /**
* Makes the text appear underlined.
*/
    UNDERLINE( 'n', "underline", true ),
    /**
* Makes the text italic.
*/
    ITALIC( 'o', "italic", true ),
    /**
* Resets all previous chat colors or formats.
*/
    RESET( 'r', "reset", true );
    /**
* The special character which prefixes all chat colour codes. Use this if
* you need to dynamically convert colour codes from your custom format.
*/
    public static final char COLOR_CHAR = '\u00A7';
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    /**
* Pattern to remove all colour codes.
*/
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile( "(?i)" + String.valueOf( COLOR_CHAR ) + "[0-9A-FK-OR]" );
    /**
* Colour instances keyed by their active character.
*/
    private static final Map<Character, ChatColor> BY_CHAR = new HashMap<Character, ChatColor>();
    /**
* The code appended to {@link #COLOR_CHAR} to make usable colour.
*/
    private final char code;
    /**
* This colour's colour char prefixed by the {@link #COLOR_CHAR}.
*/
    private final String toString;
    private final String name;
    private final boolean format;

    static
    {
        for ( ChatColor colour : values() )
        {
            BY_CHAR.put( colour.code, colour );
        }
    }

    private ChatColor(char code, String name) {
    	this(code, name, false);
    }
    
    private ChatColor(char code, String name, boolean format)
    {
        this.code = code;
        this.name = name;
        this.format = format;
        this.toString = new String( new char[]
        {
            COLOR_CHAR, code
        } );
    }

    public final String getName() {
    	return name;
    }
    
    public boolean isFormat() {
    	return this.format;
    }
    
    public char getCode() {
		return code;
	}

	@Override
    public String toString()
    {
        return toString;
    }

    /**
* Strips the given message of all color codes
*
* @param input String to strip of color
* @return A copy of the input string, without any coloring
*/
    public static String stripColor(final String input)
    {
        if ( input == null )
        {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher( input ).replaceAll( "" );
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate)
    {
        char[] b = textToTranslate.toCharArray();
        for ( int i = 0; i < b.length - 1; i++ )
        {
            if ( b[i] == altColorChar && ALL_CODES.indexOf( b[i + 1] ) > -1 )
            {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase( b[i + 1] );
            }
        }
        return new String( b );
    }

    /**
* Get the colour represented by the specified code.
*
* @param code the code to search for
* @return the mapped colour, or null if non exists
*/
    public static ChatColor getByChar(char code)
    {
        return BY_CHAR.get( code );
    }
    
    
    public static ChatColor getByColor(net.md_5.bungee.api.ChatColor c) {
		if (c != null) {
			switch(c) {
			case BLACK:
				return ChatColor.BLACK;
			case DARK_BLUE:
				return ChatColor.DARK_BLUE;
			case DARK_GREEN:
				return ChatColor.DARK_GREEN;
			case DARK_AQUA:
				return ChatColor.DARK_AQUA;
			case DARK_RED:
				return ChatColor.DARK_RED;
			case DARK_PURPLE:
				return ChatColor.DARK_PURPLE;
			case GOLD:
				return ChatColor.GOLD; 
			case GRAY:
				return ChatColor.GRAY;
			case DARK_GRAY:
				return ChatColor.DARK_GRAY;
			case BLUE:
				return ChatColor.BLUE;
			case GREEN:
				return ChatColor.GREEN;
			case AQUA:
				return ChatColor.AQUA;
			case RED:
				return ChatColor.RED;
			case LIGHT_PURPLE:
				return ChatColor.LIGHT_PURPLE;
			case YELLOW:
				return ChatColor.YELLOW;
			case WHITE:
				return ChatColor.WHITE;
			case MAGIC:
				return ChatColor.MAGIC;
			case BOLD:
				return ChatColor.BOLD;
			case STRIKETHROUGH:
				return ChatColor.STRIKETHROUGH;
			case UNDERLINE:
				return ChatColor.UNDERLINE;
			case ITALIC:
				return ChatColor.ITALIC;
			case RESET:
				return ChatColor.RESET;
			default:
				throw new IllegalStateException();
			}
		}
		throw new IllegalStateException();
    }

    public static ChatColor getByColor(org.bukkit.ChatColor c) {
		if (c != null) {
			switch(c) {
			case BLACK:
				return ChatColor.BLACK;
			case DARK_BLUE:
				return ChatColor.DARK_BLUE;
			case DARK_GREEN:
				return ChatColor.DARK_GREEN;
			case DARK_AQUA:
				return ChatColor.DARK_AQUA;
			case DARK_RED:
				return ChatColor.DARK_RED;
			case DARK_PURPLE:
				return ChatColor.DARK_PURPLE;
			case GOLD:
				return ChatColor.GOLD; 
			case GRAY:
				return ChatColor.GRAY;
			case DARK_GRAY:
				return ChatColor.DARK_GRAY;
			case BLUE:
				return ChatColor.BLUE;
			case GREEN:
				return ChatColor.GREEN;
			case AQUA:
				return ChatColor.AQUA;
			case RED:
				return ChatColor.RED;
			case LIGHT_PURPLE:
				return ChatColor.LIGHT_PURPLE;
			case YELLOW:
				return ChatColor.YELLOW;
			case WHITE:
				return ChatColor.WHITE;
			case MAGIC:
				return ChatColor.MAGIC;
			case BOLD:
				return ChatColor.BOLD;
			case STRIKETHROUGH:
				return ChatColor.STRIKETHROUGH;
			case UNDERLINE:
				return ChatColor.UNDERLINE;
			case ITALIC:
				return ChatColor.ITALIC;
			case RESET:
				return ChatColor.RESET;
			default:
				throw new IllegalStateException();
			}
		}
		throw new IllegalStateException();
    }

    
}