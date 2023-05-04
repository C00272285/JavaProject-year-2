//Display the contents of the Authors table in the books database.
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
public class Retrive extends JFrame
{
final String DATABASE_URL = "jdbc:mysql://localhost/javaproject";
  static final String USERNAME = "root";
  static final String PASSWORD = "@12July98";
  static final String DEFAULT_QUERY = "SELECT * FROM customer";
  private ResultSetTableModel  tableModel;
  private JTextArea queryArea;

  public Retrive(){
  super( "Displaying Query Results" );
  try
{
  tableModel = new ResultSetTableModel( DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY );
  queryArea = new JTextArea( DEFAULT_QUERY, 3, 100 );
  queryArea.setWrapStyleWord( true );
  queryArea.setLineWrap( true );
  JScrollPane scrollPane = new JScrollPane( queryArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
  JButton submitButton = new JButton( "Submit Query" );
  Box boxNorth = Box.createHorizontalBox();
boxNorth.add( scrollPane );
boxNorth.add( submitButton );
JTable resultTable = new JTable( tableModel );
JLabel filterLabel = new JLabel( "Filter :" );
final JTextField filterText = new JTextField();
JButton filterButton = new JButton( "Apply Filter" );
Box boxSouth = Box.createHorizontalBox();
boxSouth.add( filterLabel );
boxSouth.add( filterText );
boxSouth.add( filterButton );
add( boxNorth, BorderLayout.NORTH );
add( new JScrollPane( resultTable ), BorderLayout.CENTER );
add( boxSouth, BorderLayout.SOUTH );
submitButton.addActionListener(new ActionListener ()
{
  public void actionPerformed( ActionEvent event )
{
  try
{
  tableModel.setQuery( queryArea.getText() );
}
  catch ( SQLException sqlException )
{
  JOptionPane.showMessageDialog( null, sqlException.getMessage(),
  "Database error", JOptionPane.ERROR_MESSAGE );
  try
{
  tableModel.setQuery( DEFAULT_QUERY );
  queryArea.setText( DEFAULT_QUERY );
}
  catch ( SQLException sqlException2 )
{
  JOptionPane.showMessageDialog( null, sqlException2.getMessage(),
  "Database error", JOptionPane.ERROR_MESSAGE );
  tableModel.disconnectFromDatabase();
  System.exit ( 1 );
}
}
}
}//end Submit Button Action Listener class
);
  final TableRowSorter< TableModel > sorter =new TableRowSorter<TableModel>( tableModel );
resultTable .setRowSorter( sorter );
setSize ( 500, 250 );
setVisible ( true );
filterButton . addActionListener (new ActionListener ()
{
public void actionPerformed( ActionEvent e )
{
String text = filterText .getText();
if ( text . length () == 0 )
sorter . setRowFilter ( null );
else
{
try
{
sorter . setRowFilter (RowFilter. regexFilter ( text ) );
}
catch ( PatternSyntaxException pse )
{
JOptionPane.showMessageDialog( null, "Bad regex pattern", "Bad regex pattern", JOptionPane.ERROR_MESSAGE );
}
}
}
}//end Filter Button Action Listener class
);}
catch ( SQLException sqlException )
{
  JOptionPane.showMessageDialog( null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );
  tableModel.disconnectFromDatabase();
  System.exit ( 1 );
  }
  // dispose of window when user quits application ( this overrides the default of HIDE ON CLOSE)
  setDefaultCloseOperation ( DISPOSE_ON_CLOSE );
  addWindowListener(new WindowAdapter(){
  public void windowClosed( WindowEvent event )
{
  tableModel.disconnectFromDatabase();
  System.exit ( 0 );
}
}
);//end Window Listener class
}
  public static void main( String args [] )
{
  new Retrive();
} // end main
} // end class
  