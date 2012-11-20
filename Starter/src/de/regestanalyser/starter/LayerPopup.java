package de.regestanalyser.starter;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class LayerPopup extends JPanel implements ComponentListener
{
    public LayerPopup()
    {
        setOpaque(false);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setFocusable(true);
        setFocusCycleRoot(true);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.setColor(new Color(255,0,0,25));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        this.setSize(e.getComponent().getSize());
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
    }

    @Override
    public void componentHidden(ComponentEvent e)
    {
    }
}