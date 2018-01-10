package com.stufeed.pojo;

/**
 * Created by sowmitras on 5/11/17.
 */

public class JoinBoard {
    private String created_by;
    private String requested_by;
    private String requested_key;
    private String board_key;
    private String time_requested;
    private String user_id;

    public JoinBoard() {
    }

    public JoinBoard(String created_by, String requested_by, String requested_key, String board_key, String time_requested,String user_id) {
        this.created_by = created_by;
        this.requested_by = requested_by;
        this.requested_key = requested_key;
        this.board_key = board_key;
        this.time_requested = time_requested;
        this.user_id = user_id;
    }

    public String getTime_requested() {
        return time_requested;
    }

    public void setTime_requested(String time_requested) {
        this.time_requested = time_requested;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }

    public String getRequested_key() {
        return requested_key;
    }

    public void setRequested_key(String requested_key) {
        this.requested_key = requested_key;
    }

    public String getBoard_key() {
        return board_key;
    }

    public void setBoard_key(String board_key) {
        this.board_key = board_key;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}


/*
*
*
*
*
*
public class JoinBoards {
    private String created_by;
    private String requested_by;
    private String requested_key;
    private String board_key;
    private String time_requested;

    public JoinBoards() {
    }

    public JoinBoards(String created_by, String requested_by, String requested_key, String board_key, String time_requested) {
        this.created_by = created_by;
        this.requested_by = requested_by;
        this.requested_key = requested_key;
        this.board_key = board_key;
        this.time_requested = time_requested;
    }

    public String getTime_requested() {
        return time_requested;
    }

    public void setTime_requested(String time_requested) {
        this.time_requested = time_requested;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }

    public String getRequested_key() {
        return requested_key;
    }

    public void setRequested_key(String requested_key) {
        this.requested_key = requested_key;
    }

    public String getBoard_key() {
        return board_key;
    }

    public void setBoard_key(String board_key) {
        this.board_key = board_key;
    }
}

*/
/* DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("boards");
                        DATABASE_REFERENCE.child(model.getBoard_key()).child("members").push().setValue(model.getRequested_by());

                        DATABASE_REFERENCE = setFireBaseKey(CST_BOARD_JOIN_REQUEST);
                        DATABASE_REFERENCE.child(model.getCreated_by()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount()!=0){
                                    dataSnapshot.getChildren().iterator().hasNext();
                                    for(DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                        if(snapshot1.child("board_key").getValue().toString().equals(model.getBoard_key())){
                                            DATABASE_REFERENCE = setFireBaseKey("board_join_request");
                                            DATABASE_REFERENCE.child(model.getCreated_by()).child(snapshot1.getKey().toString()).removeValue();

                                            DATABASE_REFERENCE = setFireBaseKey("boards");
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("requested").child(snapshot1.getKey().toString()).removeValue();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });






                        final Map<String, Object> taskMap = setMapValue(""+model.getUseruid(),""+model.getRequested_by());
                        DATABASE_REFERENCE = setFireBaseKey("board_join");
                        DATABASE_REFERENCE.child(mFirebaseAdapter.getRef(position).getKey()).updateChildren(taskMap);

                        DATABASE_REFERENCE = setFireBaseKey("board_join_request");
                        DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child(String.valueOf(mFirebaseAdapter.getRef(position).getKey())).removeValue();
*/