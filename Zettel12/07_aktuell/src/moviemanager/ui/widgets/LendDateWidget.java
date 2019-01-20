package moviemanager.ui.widgets;

import java.util.Date;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

import com.ibm.icu.text.DateFormat;

import moviemanager.data.Movie;

/**
 * Widget for handling the watch date of a given movie.
 *
 */
public class LendDateWidget extends LabelActionWidget {

    public LendDateWidget(Object handledObject, Composite parent, int style) {
        super(handledObject, parent, style);
    }

    @Override
    protected void updateWidgets() {
        Movie movie = (Movie) this.handledObject;
        Date returnDate = movie.getReturnDate();

        if (actionLinkListener != null) {
            actionLink.removeSelectionListener(actionLinkListener);
        }
        
        if (movie.getIstVerliehen() == false) {
          actionLink.setText("<A>Lend movie</A>");
        }else {
          actionLink.setText("<A>Return movie</A>");
        }
        actionLinkListener = new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
              
                if (movie.getIstVerliehen() == false) {
                  movie.setReturnDate(14);
                }
                else {
                  movie.setReturnDate();
                }
                updateWidgets();
            }
        };
        actionLink.addSelectionListener(actionLinkListener);
        if (movie.getIstVerliehen() == false) {
            label.setText("This movie can be lend yet.");
        } else {
            label.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(returnDate));
        }
        layout();
    }

}
