package bibviz.visualization;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JDialog;

import net.miginfocom.swing.MigLayout;

public class FieldSelectionDialog extends JDialog {

	private List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

	public FieldSelectionDialog() {
		setLayout(new MigLayout());
		setUndecorated(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);

		String[] fields = new String[] { "title", "abstract" };
		for (String s : fields) {
			JCheckBox fieldBox = new JCheckBox(s);
			checkBoxes.add(fieldBox);
			fieldBox.setSelected(true);
			this.add(fieldBox, "wrap");
		}
		this.pack();
	}

	public List<String> getSelectedFields() {
		List<String> selectedFields = new ArrayList<String>();
		for (JCheckBox checkBox : checkBoxes) {
			if (checkBox.isSelected())
				selectedFields.add(checkBox.getText());
		}
		return selectedFields;
	}
}
