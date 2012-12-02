package de.regestanalyser.starter;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

/**
 * This class produces a JPanel that is used as Layer above all Components
 * within the program's Window to signalize that some processes are still
 * working and the user has to wait for them to finish.
 * 
 * @author nils
 * 
 */
@SuppressWarnings("serial")
class WaitingLayer extends JPanel implements ComponentListener {
	public WaitingLayer() {
		setOpaque(false);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setFocusable(true);
		setFocusCycleRoot(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(255, 0, 0, 25));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.setSize(e.getComponent().getSize());
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}