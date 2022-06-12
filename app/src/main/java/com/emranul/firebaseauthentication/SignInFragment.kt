package com.emranul.firebaseauthentication

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.emranul.firebaseauthentication.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    var email = ""
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnregister.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging In....")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click being login
        binding.btnSignIn.setOnClickListener {
            //before login
            validateData()


        }
    }

    private fun validateData() {
        //get data
        email = binding.signinemailid.text.toString().trim()
        password = binding.editTxtPass.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailIl.error = "Please Enter valid Email"
        } else if (TextUtils.isEmpty(password)) {
            binding.etPasswordLayout.error = "Please enter password"
        } else {
            firebaseLogin()
        }


    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
            //login success
            progressDialog.dismiss()
                Toast.makeText(requireContext(), "Success fully Logged in ", Toast.LENGTH_SHORT).show()
                //get user info
                val firebaseUser=firebaseAuth.currentUser
                val email=firebaseUser!!.email
                //intent signin to home fragment
                Intent(requireContext(),HomeFragment::class.java).apply {
                    startActivity(this)
                }
                requireActivity().finish()


            }
            .addOnFailureListener {
                //login failed
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Login Failed due to {${it.message}}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //if user already logged in go to home activity
        //get current user
        val firebaseuser = firebaseAuth.currentUser
        if (firebaseuser != null) {
            //user is logged in
            Intent(context, HomeFragment::class.java).apply {
                startActivity(this)

            }
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}