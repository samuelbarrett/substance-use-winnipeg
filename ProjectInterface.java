import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ProjectInterface extends JFrame 
{
    // JFrame
    static JFrame f;
  
    // JButton
    static JButton b, b1, b2, b3;
  
    // Label to display text
    static JLabel l;
  
	// constructor
    public ProjectInterface() {}

	public void gui() {
		JFrame frame = new JFrame("3380 Project: Group 6");

		JPanel panel = new JPanel();
        panel.setLayout(null);

        // big header
        JLabel h = new JLabel("Group 6: Substance Use");
        h.setFont(new Font("SansSerif", Font.PLAIN, 25));
        Dimension size = h.getPreferredSize();
        h.setBounds(360, 5, 300, 50);
        panel.add(h);

        // queries header
        JLabel lh = new JLabel("Queries");
        lh.setFont(new Font("SansSerif", Font.PLAIN, 15));
        size = lh.getPreferredSize();
        lh.setBounds(220, 70, 100, size.height);
        panel.add(lh);

        // results header
        JLabel rh = new JLabel("Results");
        rh.setFont(new Font("SansSerif", Font.PLAIN, 15));
        size = rh.getPreferredSize();
        rh.setBounds(700, 70, 100, size.height);
        panel.add(rh);

        // query #1
        JLabel q1 = new JLabel("1. Which wards have the most narcan incidents?");
        size = q1.getPreferredSize();
        q1.setBounds(100, 130, size.width, size.height);
        panel.add(q1);

        // button for q1
        JButton b1 = new JButton("Run");
        size = b1.getPreferredSize();
        b1.setBounds(30, 125, size.width, size.height);
        panel.add(b1);

        // query #2
        JLabel q2 = new JLabel("2. Which age groups have the most narcan incidents?");
        size = q2.getPreferredSize();
        q2.setBounds(100, 190, size.width, size.height);
        panel.add(q2);

        // button for q2
        JButton b2 = new JButton("Run");
        size = b2.getPreferredSize();
        b2.setBounds(30, 185, size.width, size.height);
        panel.add(b2);

        // query #3
        JLabel q3 = new JLabel("3. What are the biggest drug party busts (3+ people)");
        size = q3.getPreferredSize();
        q3.setBounds(100, 250, size.width, size.height);
        panel.add(q3);

        // button for q3
        JButton b3 = new JButton("Run");
        size = b3.getPreferredSize();
        b3.setBounds(30, 245, size.width, size.height);
        panel.add(b3);

        // query #4
        JLabel q4 = new JLabel("4. Which substances were at the parties people get wasted in?");
        size = q4.getPreferredSize();
        q4.setBounds(100, 310, size.width, size.height);
        panel.add(q4);

        // button for q4
        JButton b4 = new JButton("Run");
        size = b4.getPreferredSize();
        b4.setBounds(30, 305, size.width, size.height);
        panel.add(b4);

        // query #5
        JLabel q5 = new JLabel("5. How many parties did each ward have?");
        size = q5.getPreferredSize();
        q5.setBounds(100, 370, size.width, size.height);
        panel.add(q5);

        // button for q5
        JButton b5 = new JButton("Run");
        size = b5.getPreferredSize();
        b5.setBounds(30, 365, size.width, size.height);
        panel.add(b5);

        // query #6
        JLabel q6 = new JLabel("6. What are the most common areas for X substance?");
        size = q6.getPreferredSize();
        q6.setBounds(100, 430, size.width, size.height);
        panel.add(q6);

        // button for q6
        JButton b6 = new JButton("Run");
        size = b6.getPreferredSize();
        b6.setBounds(30, 425, size.width, size.height);
        panel.add(b6);

        // query #7
        JLabel q7 = new JLabel("7. What are the most common age/substance combinations?");
        size = q7.getPreferredSize();
        q7.setBounds(100, 490, size.width, size.height);
        panel.add(q7);

        // button for q7
        JButton b7 = new JButton("Run");
        size = b7.getPreferredSize();
        b7.setBounds(30, 485, size.width, size.height);
        panel.add(b7);

        // query #8
        JLabel q8 = new JLabel("8. What are the most common age groups for substance X?");
        size = q8.getPreferredSize();
        q8.setBounds(100, 550, size.width, size.height);
        panel.add(q8);

        // button for q8
        JButton b8 = new JButton("Run");
        size = b8.getPreferredSize();
        b8.setBounds(30, 545, size.width, size.height);
        panel.add(b8);

        // query #9
        JLabel q9 = new JLabel("9. Which holidays have the highest prevalence of substance use?");
        size = q9.getPreferredSize();
        q9.setBounds(100, 610, size.width, size.height);
        panel.add(q9);

        // button for q9
        JButton b9 = new JButton("Run");
        size = b9.getPreferredSize();
        b9.setBounds(30, 605, size.width, size.height);
        panel.add(b9);

        // query #10
        JLabel q10 = new JLabel("10. What hour of the day is the most common for substance use?");
        size = q10.getPreferredSize();
        q10.setBounds(100, 670, size.width, size.height);
        panel.add(q10);

        // button for q10
        JButton b10 = new JButton("Run");
        size = b10.getPreferredSize();
        b10.setBounds(30, 665, size.width, size.height);
        panel.add(b10);

        // Patient button
        JButton p = new JButton("Patient Table");
        size = p.getPreferredSize();
        p.setBounds(370, 720, size.width, size.height);
        panel.add(p);

        // Incident button
        JButton i = new JButton("Incident Table");
        size = i.getPreferredSize();
        i.setBounds(245, 720, size.width, size.height);
        panel.add(i);

        // substances button
        JButton s = new JButton("Substance Table");
        size = s.getPreferredSize();
        s.setBounds(104, 720, size.width, size.height);
        panel.add(s);

        // Neighbourhood button
        JButton n = new JButton("Neighbourhood Table");
        size = n.getPreferredSize();
        n.setBounds(490, 720, 170, size.height);
        panel.add(n);
        
        // Ward button
        JButton w = new JButton("Ward Table");
        size = w.getPreferredSize();
        w.setBounds(672, 720, size.width, size.height);
        panel.add(w);

        // Consumes button
        JButton c = new JButton("Consumes Table");
        size = c.getPreferredSize();
        c.setBounds(782, 720, size.width, size.height);
        panel.add(c);

        // download results
        JButton d = new JButton("Download Results");
        size = d.getPreferredSize();
        d.setBounds(660, 100, size.width, size.height);
        panel.add(d);

		frame.setVisible(true);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("3380 Project: Group 6");
		frame.pack();
		frame.setSize(1000, 800);
	}
}
