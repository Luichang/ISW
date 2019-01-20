package moviemanager.ui.widgets;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

import moviemanager.data.Movie;

/**
 * Widget for handling the watch date of a given movie.
 *
 */
public class EditReturnDateWidget extends LabelActionWidget {

    public EditReturnDateWidget(Object handledObject, Composite parent, int style) {
        super(handledObject, parent, style);
    }

    @Override
    protected void updateWidgets() {
        Movie movie = (Movie) this.handledObject;
        //Date returnDate = movie.getReturnDate();

        if (actionLinkListener != null) {
            actionLink.removeSelectionListener(actionLinkListener);
        }
        actionLink.setText("<A>Extend Lended Date</A>");
        System.out.println(movie.getIstVerliehen());
        if (movie.getIstVerliehen() == true) {
          actionLink.setText("<A>Extend Lended Date</A>");
          
        }
        else {
          actionLink.setText("<A></A>"); 
        }
        actionLinkListener = new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
              
                if (movie.getIstVerliehen() == true) {
                  movie.setReturnDate(7);
                }
                updateWidgets();
            }
        };
        actionLink.addSelectionListener(actionLinkListener);
    }

}
