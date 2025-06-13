import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.eventapp.R
import com.example.eventapp.ExtraFiles.UserTypeLogin

class OrganizerLogout : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_organizer_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLogoutConfirmationDialog()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, _ ->
                logoutUser()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                // Navigate back or to home fragment
                requireActivity().supportFragmentManager.popBackStack()
            }
            .setCancelable(false)
            .show()
    }

    private fun logoutUser() {
        val sharedPref = requireActivity().getSharedPreferences("eventAppPrefs", AppCompatActivity.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), UserTypeLogin::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }
}
