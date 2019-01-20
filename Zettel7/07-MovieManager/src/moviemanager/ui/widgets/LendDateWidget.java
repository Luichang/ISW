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
        actionLink.setText("<A>Watch movie</A>");
        actionLinkListener = new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Maybe use a progress dialog to simulate watching the
                // movie?
                movie.setWatchDate(new Date());
                updateWidgets();
            }
        };
        actionLink.addSelectionListener(actionLinkListener);
        if (returnDate != null) {
            label.setText("This movie can not be lend yet.");
        } else {
            label.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(returnDate));
        }
        layout();
    }

}
