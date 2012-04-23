package tikainaction.chapter6;

import org.apache.tika.cli.TikaCLI;

/**
 *
 * Print the supported Tika Metadata models and
 * their fields.
 *
 */
//<start id="met_lister"/> 
public class DescribeMetadata {
  
  public static void main(String [] args) throws Exception{
     TikaCLI.main(new String[]{"--list-met-models"});
  }

}
//<end id="met_lister"/>
