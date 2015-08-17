/**
 * java.awt does not have a RadioMenuItem which can be used with the System
 * Tray, so I made one. This class extends java.awt.CheckboxMenuItem, and
 * therefore acts very similar. The difference is that only one radio menu
 * item can be checked per group. This is meant to be used with
 * the RadioMenuItemGroup class you should have received with this file.
 *
 * @title RadioMenuItem.java
 * @author uPaymeiFixit
 * @version 1.0
 * @since 2015-08-16
 */

import java.awt.CheckboxMenuItem;

public class RadioMenuItem extends CheckboxMenuItem
{
	private RadioMenuItemGroup group;
	public static final long serialVersionUID = 42L; // I don't know what this is but I needed it apparently.

	public RadioMenuItem()
	{
		this(null, new RadioMenuItemGroup(), false);
	}
	public RadioMenuItem(String label)
	{
		this(label, new RadioMenuItemGroup(), false);
	}
	public RadioMenuItem(String label, RadioMenuItemGroup group)
	{
		this(label, group, false);
	}
	public RadioMenuItem(String label, boolean state)
	{
		this(label, new RadioMenuItemGroup(), state);
	}
	public RadioMenuItem(String label, RadioMenuItemGroup group, boolean state)
	{
		super(label, state);
		this.group = group;
		this.group.addRadioMenuItem(this);
	}

	public RadioMenuItemGroup getRadioMenuItemGroup()
	{
		return group;
	}

	public void setRadioMenuItemGroup(RadioMenuItemGroup group)
	{
		this.group = group;
		group.addRadioMenuItem(this);
	}
}
