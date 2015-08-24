/**
 * java.awt does not have a RadioMenuItem which can be used with the System
 * Tray, so I made one. This class extends allows you to make full use of the
 * RadioMenuItem class you should have received with this file by grouping
 * them together in one object so only one can be set “on” at a time.
 *
 * @title RadioMenuItemGroup.java
 * @author uPaymeiFixit
 * @version 1.2
 * @since 2015-08-17
 */

import java.util.ArrayList;

public class RadioMenuItemGroup
{
	ArrayList<RadioMenuItem> radios;

	// Default methods from java.awt.CheckboxGroup
	public RadioMenuItemGroup()
	{
		radios = new ArrayList<RadioMenuItem>();
	}

	// Gets the current choice from this radio menu item group. The current
	// choice is the radio menu item in this group that is currently in the "on"
	// state, or null if all radio menu itemes in the group are off.
	public RadioMenuItem getSelectedRadioMenuItem()
	{
		for ( RadioMenuItem radio : radios )
			if ( radio.getState() )
				return radio;
		return null;
	}

	// Sets the currently selected radio menu item in this group to be the
	// specified radio menu item. This method sets the state of that radio menu
	// item to "on" and sets all other radio menu itemes in the group to be off.
	// If the radio menu item argument is null, all radio menu itemes in this
	// radio menu item group are deselected. If the radio menu item argument
	// belongs to a different radio menu item group, this method does nothing.
	public void setSelectedRadioMenuItem( RadioMenuItem radio )
	{
		// If the user gave us a random RadioMenuItem not in this group, add it
		if ( radio.getRadioMenuItemGroup() != this )
			radio.setRadioMenuItemGroup( this );

		// Set all of the radios to off
		for ( RadioMenuItem local_radio : radios )
			local_radio.setState( false );

		// Turn our radio on
		radio.setState( true );
	}

	// Returns a string representation of this radio menu item group, including
	// the value of its current selection.
	public String toString()
	{
		// I don't know exactly how CheckboxGroup's toString works
		String output = "[";
		for ( RadioMenuItem radio : radios )
			output += radio.getLabel() + ": " + radio.getState() + ", ";
		return output.substring( 0, output.length() - 2 )+"]";
	}

	// Custom methods
	public void addRadioMenuItem( RadioMenuItem radio )
	{
		// If the user gave us a random RadioMenuItem not in this group, add it
		if ( radio.getRadioMenuItemGroup() != this )
		{
			radio.setRadioMenuItemGroup( this );
		}
		else
		{
			radios.add( radio );

			if ( radio.getState() )
			{
				setSelectedRadioMenuItem( radio );
			}
		}
	}
}
