package com.blockdev.birdy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.blockdev.birdy.adaper.OptionAdapter
import com.blockdev.birdy.model.MyFirebase.getUser
import com.blockdev.birdy.model.MyFirebase.myUid
import com.blockdev.birdy.ui.AddFriendActivity
import com.blockdev.birdy.ui.LogInActivity
import com.blockdev.birdy.ui.NewPostActivity
import com.google.firebase.auth.FirebaseAuth

class FragmentProfile : Fragment(), View.OnClickListener, OnItemClickListener {

    private val options = arrayOf(
        "New post",
        "Friend request",
        "Friends",
        "Settings",
        "About",
        "Log out"
    )
    private val icons = intArrayOf(
        R.drawable.ic_new_post,
        R.drawable.ic_add_circle,
        R.drawable.ic_friend,
        R.drawable.ic_settings,
        R.drawable.ic_about,
        R.drawable.ic_log_out,
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val profileLayout = rootView.findViewById<View>(R.id.pf_bar) as RelativeLayout
        val listView = rootView.findViewById<RecyclerView>(R.id.pf_list_view)
        val myName = rootView.findViewById<View>(R.id.pf_txt_name) as TextView
        val email = rootView.findViewById<View>(R.id.pf_txt_email) as TextView
        profileLayout.setOnClickListener(this)
        myName.text = getUser(myUid).name
        email.text = FirebaseAuth.getInstance().currentUser!!.email
        val adapter = OptionAdapter(options, icons)
        listView.adapter = adapter
        return rootView
    }

    override fun onClick(v: View) {
        if (v.id == R.id.pf_bar) {
            // // TODO: 05-Jun-17 create activity profile
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        when (position) {
            0 -> startActivity(Intent(activity, NewPostActivity::class.java))
            1 -> startActivity(Intent(activity, AddFriendActivity::class.java))
            2 -> {}
            5 -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(activity, LogInActivity::class.java))
                requireActivity().finish()
            }
        }
    }
} // /////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, username: String, email: String) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_logo_blue),
                contentDescription = ""
            )
            Text(modifier = Modifier.fillMaxWidth(), text = username, fontWeight = FontWeight.Bold)
            Text(modifier = Modifier.fillMaxWidth(), text = email)
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(modifier = Modifier, username = "Saka", email = "email@example.com")
}
