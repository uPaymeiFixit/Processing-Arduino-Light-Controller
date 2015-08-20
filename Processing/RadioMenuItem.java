/**
 * java.awt does not have a RadioMenuItem which can be used with the System
 * Tray, so I made one. This class extends java.awt.CheckboxMenuItem, and
 * therefore acts very similar. The difference is that only one radio menu
 * item can be checked per group. This is meant to be used with
 * the RadioMenuItemGroup class you should have received with this file.
 *
 * @title RadioMenuItem.java
 * @author uPaymeiFixit
 * @version 1.2
 * @since 2015-08-17
 */

import java.awt.CheckboxMenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RadioMenuItem extends CheckboxMenuItem
{
	private ItemListener listener;
	private RadioMenuItemGroup group;

	 // I don't know what this is but I needed it apparently.
	public static final long serialVersionUID = 42L;

	public RadioMenuItem()
	{
		super();
	}
	public RadioMenuItem( String label )
	{
		super( label );
	}
	public RadioMenuItem( String label, RadioMenuItemGroup group )
	{
		super( label );
		setRadioMenuItemGroup( group );
	}
	public RadioMenuItem( String label, boolean state )
	{
		super( label, state );
	}
	public RadioMenuItem( String label, RadioMenuItemGroup group, boolean state )
	{
		super( label, state );
		setRadioMenuItemGroup( group );
	}

	public RadioMenuItemGroup getRadioMenuItemGroup()
	{
		return group;
	}

	public void setRadioMenuItemGroup( RadioMenuItemGroup group )
	{
		this.group = group;
		this.removeItemListener( listener );
		if ( group != null )
		{
			// We need to make sure "this" is final so that we can refer to it
			// in the scope below. Some compilers make you do this.
			final RadioMenuItem me = this;
			listener = new ItemListener()
			{
				@Override
				public void itemStateChanged( ItemEvent e )
				{
					if ( e.getStateChange() == ItemEvent.SELECTED )
					{
						me.group.setSelectedRadioMenuItem( me );
					}
				}
			};
			this.addItemListener( listener );
			group.addRadioMenuItem( this );
		}
	}
}
