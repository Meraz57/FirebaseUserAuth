package com.emranul.firebaseauthentication

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emranul.firebaseauthentication.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    //progress bar
    private lateinit var progressDialog: ProgressDialog

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth
    private var username = ""
    private var email = ""
    private var password = ""


    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnlogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        //configure progressbar
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating account in ..")
        progressDialog.setCanceledOnTouchOutside(false)

        //
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click before signup
        binding.signupbuttonid.setOnClickListener {
            //validateData
            validateData()


        }


    }

    private fun validateData() {

        username = binding.signupusernameid.text.toString().trim()
        email = binding.signupemailid.text.toString().trim()
        password = binding.signuppasswordid.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailIl.error = "Please Enter valid Email"
        } else if (TextUtils.isEmpty(password)) {
            binding.etPasswordLayout.error = "Please enter password"
        }
        /*else if (Regex("^[a-z._]+$", RegexOption.IGNORE_CASE).matches(username) &&
            !username.startsWith(".") &&
            !username.startsWith("_") &&
            !username.startsWith(".") &&
            !username.endsWith("_") &&
            !username.contains("..") &&
            !username.contains("__") &&
            !username.contains("._") &&
            !username.contains("_.")){

        }*/
        else if (password.length < 6) {
            binding.etPasswordLayout.error = "Password must at last 6 character"

        } else {
            firebaseSignup()
        }

    }

    private fun firebaseSignup() {
        //show progressbar
        progressDialog.show()

       //create a account
       firebaseAuth.createUserWithEmailAndPassword(email, password)
           .addOnSuccessListener {
            progressDialog.dismiss()
               val firebaseUser=firebaseAuth.currentUser
               val email= firebaseUser!!.email
               Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
           }
           .addOnFailureListener {
        progressDialog.dismiss()

               Log.d("TAG", "firebaseSignup: ${it.message}")
               Toast.makeText(requireContext(), "Signup Failed due to ${it.message}", Toast.LENGTH_SHORT).show()

           }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}