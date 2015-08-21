package vn.spring.zebra.um.gui;

import java.awt.*;

import javax.swing.*;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningStyleImagePane extends JScrollPane implements UIDispose {
	private static final long serialVersionUID = 1L;
	
	private static final Dimension gap = new Dimension(20, 20);
	public static final String VERBAL_VISUAL_IMAGE = ZebraStatic.IMAGE_DIR + "verbal_visual.gif";
	public static final String ACTIVIST_REFLECTOR_IMAGE = ZebraStatic.IMAGE_DIR + "activist_reflector.gif";
	public static final String PRAGMATIST_THEORIST_IMAGE = ZebraStatic.IMAGE_DIR + "pragmatist_theorist.gif";

	private Image verbal_visual = null;
	private Image activist_reflector = null;
	private Image pragmatist_theorist = null;

	public void dispose() {}
	public LearningStyleImagePane() {
		super();
		setAutoscrolls(true);
		
		verbal_visual = Toolkit.getDefaultToolkit().
			getImage(getClass().getResource(VERBAL_VISUAL_IMAGE));
		activist_reflector = Toolkit.getDefaultToolkit().
			getImage(getClass().getResource(ACTIVIST_REFLECTOR_IMAGE));
		pragmatist_theorist = Toolkit.getDefaultToolkit().
			getImage(getClass().getResource(PRAGMATIST_THEORIST_IMAGE));
		
		while(verbal_visual.getHeight(this) == -1) {}
		while(activist_reflector.getHeight(this) == -1) {}
		while(pragmatist_theorist.getHeight(this) == -1) {}
		
		Dimension size = new Dimension(0, 0);
		size.width   = Math.max(size.width, verbal_visual.getWidth(this));       size.height += verbal_visual.getHeight(this);
		size.height += gap.height;
		size.width   = Math.max(size.width, activist_reflector.getWidth(this));  size.height += activist_reflector.getHeight(this);
		size.height += gap.height;
		size.width   = Math.max(size.width, pragmatist_theorist.getWidth(this)); size.height += pragmatist_theorist.getHeight(this);
		
		size.width  += 2*gap.width;
		size.height += 2*gap.height;
		
		
		setSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Point pos = new Point(0, 0);
		
		pos.x += gap.width; pos.y += gap.height;
		g.drawImage(verbal_visual, pos.x, pos.y, verbal_visual.getWidth(this), verbal_visual.getHeight(this), this);
		
		pos.y += verbal_visual.getHeight(this) + gap.height;
		g.drawImage(activist_reflector, pos.x, pos.y, activist_reflector.getWidth(this), activist_reflector.getHeight(this), this);

		pos.y += activist_reflector.getHeight(this) + gap.height;
		g.drawImage(pragmatist_theorist, pos.x, pos.y, pragmatist_theorist.getWidth(this), pragmatist_theorist.getHeight(this), this);
	}

}
