package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.chat.IndexObserver;
import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.contracts.ChatroomContract;
import edu.stevens.cs522.chat.dialog.CreateChatRoom;
import edu.stevens.cs522.chat.managers.TypedCursor;

public class IndexFragment<T> extends ListFragment implements IIndexManager.Callback<T> {

    @SuppressWarnings("unused")
    private final static String TAG = IndexFragment.class.getCanonicalName();

    //The serialization (saved instance state) Bundle key representing the activated item position. Only used on tablets.
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * A dummy implementation of the {@link IIndexManager} interface that does nothing. Used only when this fragment is not
     * attached to an activity.
     */
    private IIndexManager<T> dummyManager = new IIndexManager<T>() {
        @Override
        public void onItemSelected(T item) {
        }

        @Override
        public SimpleCursorAdapter getIndexTitles(Callback<T> callback) {
            return new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, null, null, null, 0);
        }
    };

    //The fragment's current callback object, which is notified of list item clicks.
    private IIndexManager<T> indexManager = dummyManager;
     //The current activated item position. Only used on tablets.
    private int activatedPosition = ListView.INVALID_POSITION;
    private TextView title;
    private View addChatRoomeButton;
    private Activity activity;
    private SimpleCursorAdapter adapter;
    private TypedCursor<T> cursor;
    private IndexObserver observer;

     //Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
    public IndexFragment() {
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        this.activity = (Activity) context;

        if (!(context instanceof IIndexManager)) {
            throw new IllegalStateException("Context must implement IIndexManager.");
        }

        indexManager = (IIndexManager<T>) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_index, container, false);

        String[] from = {ChatroomContract.NAME};
        int[] to = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(activity, android.R.layout.simple_list_item_activated_1, null, from, to, 0);

        title = (TextView) rootView.findViewById(R.id.index_title);
        addChatRoomeButton = rootView.findViewById(R.id.add_chatroom_button);
        addChatRoomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChatRoom.launch(activity, "");
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Called after onCreateView() returns.
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Call to getActivity() must wait until activity is created.
        indexManager.getIndexTitles(this);
        // this.adapter = indexManager.getIndexTitles(this);
        // setListAdapter(adapter);

    }

    public void setIndexTitle(String title) {
        if (this.title != null) {
            this.title.setText(title);
        }
    }

    @Override
    public void setTitles(TypedCursor<T> cursor) {
        this.cursor = cursor;
        adapter.swapCursor(cursor.getCursor());
        setListAdapter(adapter);
        this.observer = new IndexObserver(adapter, getListView());
        cursor.registerContentObserver(observer);
    }

    public void clearTitles() {
        // TODO?
//		this.cursor.close();
        adapter.swapCursor(null);
        cursor.unregisterContentObserver(observer);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        indexManager = dummyManager;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        T item = cursor.create((Cursor)getListView().getItemAtPosition(position));
        indexManager.onItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, activatedPosition);
        }
    }

     //Turns on activate-on-click mode. When this mode is on, list items will be given the 'activated' state when touched.
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(activatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        activatedPosition = position;
    }

}