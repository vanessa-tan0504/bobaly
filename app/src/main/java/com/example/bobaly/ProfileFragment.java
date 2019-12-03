//Fragment code for menu (bottom navigation)
package com.example.bobaly;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProfileFragment extends Fragment {
    private Button btnLogout,btnDeleteAcc,btnChgProfile;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userUID;
    private FirebaseUser currentUser;
    private CollectionReference itemsRef;
    private Query query;
    private TextView email,user;
    private EditText editpass,cfmpass,edituser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);

        btnLogout=v.findViewById(R.id.profile_logout);
        btnDeleteAcc=v.findViewById(R.id.profile_deleteAcc);
        btnChgProfile=v.findViewById(R.id.profile_update);
        email=v.findViewById(R.id.profile_email);
        user=v.findViewById(R.id.profile_user);
        editpass=v.findViewById(R.id.profile_chg_pw);
        cfmpass=v.findViewById(R.id.profile_cfm_pw);
        edituser=v.findViewById(R.id.profile_chg_user);
        auth=FirebaseAuth.getInstance();

        //get instance of firestore
        db=FirebaseFirestore.getInstance();

        //get user uid
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        userUID=currentUser.getUid();

        //declare collection ref
        itemsRef=db.collection("orders");
        query=itemsRef.whereEqualTo("order",userUID+"");

        //set textview in profile
        email.setText(currentUser.getEmail().toString());
        user.setText(currentUser.getDisplayName().toString());


        //logout account
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity(),R.style.AlertDialogStyle).setTitle("Confirm Logout")
                        .setMessage("Confirm logout?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(),LogInActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setIcon(R.drawable.logout)
                        .show();
            }
        });
        //end of logout account

        //delete account
        btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity(),R.style.AlertDialogStyle).setTitle("Confirm Delete Account")
                        .setMessage("Confirm delete account? Account won't be retrieved back if deleted.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete user's account
                                if(currentUser!=null) {
                                    currentUser.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getActivity(), LogInActivity.class); //delete account and go to login page
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    } else {
                                                        Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setIcon(R.drawable.deleteacc)
                        .show();
            }
        });
        //end of delete account


        //change password or username
        btnChgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(!TextUtils.isEmpty(editpass))
                //chg password
                final String newpass = editpass.getText().toString().trim();
                final String newcfmpass = cfmpass.getText().toString().trim();
                final String newname = edituser.getText().toString().trim();

                if(newpass.length() < 6 && newcfmpass.length()>0) { //if new password too short
                    editpass.setError("Password too short");
                    return;
                }
                if(!TextUtils.isEmpty(newpass)){ //if new password got something. only check confirm password
                  if(!newpass.equals(newcfmpass)) { //if confirm password different with new password
                      cfmpass.setError("Confirm password not same with password");
                      return;
                  }
                }

                //case 1 :if new password and confirm password not empty, but username empty
                if((!TextUtils.isEmpty(newcfmpass))&&(!TextUtils.isEmpty(newpass))&&(TextUtils.isEmpty(newname))){
                   new AlertDialog.Builder(getActivity(),R.style.AlertDialogStyle).setTitle("Edit Password")
                           .setMessage("Confirm applied changes to new password "+newpass+" ?")
                           .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                   progressDialog.setMessage("Updating profile...");
                                   progressDialog.show();

                                   //start updateing password
                                   currentUser.updatePassword(newpass)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()) {
                                                       progressDialog.dismiss();
                                                       Toast.makeText(getContext(), "Password successfully changed, please log-in again", Toast.LENGTH_SHORT).show();
                                                       auth.getInstance().signOut();
                                                       Intent intent = new Intent(getActivity(), LogInActivity.class); //change pw then go to login page
                                                       startActivity(intent);
                                                       getActivity().finish();
                                                   } else {
                                                       Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           });
                               }
                           })
                           .setNegativeButton("Cancel",null)
                           .setIcon(R.drawable.edit)
                           .show();
                   return;
                 }
                //end of case 1 :if new password and confirm password not empty, but username empty

                 //case 2: if username not empty,but new password and confirm password empty,
                 else if((TextUtils.isEmpty(newcfmpass))&&(TextUtils.isEmpty(newpass))&&(!TextUtils.isEmpty(newname))){
                    new AlertDialog.Builder(getActivity(),R.style.AlertDialogStyle).setTitle("Edit Username")
                            .setMessage("Confirm applied changes to username "+newname+" ?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setMessage("Updating profile...");
                                    progressDialog.show();

                                    //start update username
                                    UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()
                                            .setDisplayName(edituser.getText().toString().trim())
                                            .build();
                                    currentUser.updateProfile(profileChangeRequest)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getContext(), "Username successfully changed", Toast.LENGTH_SHORT).show();
                                                        FragmentTransaction ft = getFragmentManager().beginTransaction(); //refresh fragment page
                                                        ft.detach(ProfileFragment.this).attach(ProfileFragment.this).commit(); //refresh fragment page
                                                    }
                                                    else{
                                                        Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .setIcon(R.drawable.edit)
                            .show();
                    return;
                }
                //end of case 2: if username not empty,but new password and confirm password empty,

                //case 3 : if username, new password and confirm password not empty
                else if((!TextUtils.isEmpty(newcfmpass))&&(!TextUtils.isEmpty(newpass))&&(!TextUtils.isEmpty(newname))){
                    new AlertDialog.Builder(getActivity(),R.style.AlertDialogStyle).setTitle("Edit Profile")
                            .setMessage("Confirm applied changes to username " +newname+ " and password " +newpass+" ?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setMessage("Updating profile...");
                                    progressDialog.show();

                                    //start update password
                                    currentUser.updatePassword(newpass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()//change username
                                                        .setDisplayName(edituser.getText().toString().trim())
                                                        .build();
                                                //start update username
                                                currentUser.updateProfile(profileChangeRequest)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(getContext(), "Password and Username successfully changed, please log-in again", Toast.LENGTH_SHORT).show();
                                                                    auth.getInstance().signOut();
                                                                    Intent intent = new Intent(getActivity(), LogInActivity.class); //change pw then go to login page
                                                                    startActivity(intent);
                                                                    getActivity().finish();
                                                                }
                                                                else{
                                                                    Toast.makeText(getContext(), "username cnnot chg", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                            }
                                            else{
                                                Toast.makeText(getContext(), "pw cnnot chg", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .setIcon(R.drawable.edit)
                            .show();
                    return;
                }
                //end of case 3 : if username, new password and confirm password not empty

                else{ //if all no enter field
                    Toast.makeText(getActivity(), "Fill in field to edit profile", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        //end of change password or username

        return v;
    }
}
