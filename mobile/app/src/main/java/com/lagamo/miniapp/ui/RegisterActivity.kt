package com.lagamo.miniapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lagamo.miniapp.R
import com.lagamo.miniapp.model.RegisterRequest
import com.lagamo.miniapp.repository.AuthRepository
import com.lagamo.miniapp.repository.TokenManager
import kotlinx.coroutines.launch

/**
 * Register Screen — 3-step wizard matching web app design.
 *
 * Step 1: Personal Info (firstname, middlename, lastname)
 * Step 2: Address (street, barangay, municipality, province, country)
 * Step 3: Credentials (contact, email, password, confirm password)
 *
 * Features:
 * - Animated step indicator (active=magenta, completed=cyan)
 * - Real-time password strength validation
 * - Confirm password matching
 * - Custom toast notifications
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    // Step containers
    private lateinit var step1: LinearLayout
    private lateinit var step2: LinearLayout
    private lateinit var step3: LinearLayout

    // Step indicator views
    private lateinit var stepCircle1: TextView
    private lateinit var stepCircle2: TextView
    private lateinit var stepCircle3: TextView
    private lateinit var stepLine1: View
    private lateinit var stepLine2: View
    private lateinit var txtStepName: TextView

    // Buttons
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

    // Inputs
    private lateinit var inputFirstname: EditText
    private lateinit var inputMiddlename: EditText
    private lateinit var inputLastname: EditText
    private lateinit var inputStreet: EditText
    private lateinit var inputBarangay: EditText
    private lateinit var inputMunicipality: EditText
    private lateinit var inputProvince: EditText
    private lateinit var inputCountry: EditText
    private lateinit var inputContact: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText

    // Password rules
    private lateinit var pwRulesContainer: LinearLayout
    private lateinit var pwRuleLength: TextView
    private lateinit var pwRuleUpper: TextView
    private lateinit var pwRuleLower: TextView
    private lateinit var pwRuleNumber: TextView
    private lateinit var pwRuleSpecial: TextView

    private var currentStep = 1
    private val stepNames = arrayOf("Personal Info", "Address", "Credentials")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tokenManager = TokenManager(this)
        authRepository = AuthRepository(tokenManager)

        initViews()
        setupListeners()
        updateStepUI()
    }

    private fun initViews() {
        step1 = findViewById(R.id.step1_container)
        step2 = findViewById(R.id.step2_container)
        step3 = findViewById(R.id.step3_container)

        stepCircle1 = findViewById(R.id.step_circle_1)
        stepCircle2 = findViewById(R.id.step_circle_2)
        stepCircle3 = findViewById(R.id.step_circle_3)
        stepLine1 = findViewById(R.id.step_line_1)
        stepLine2 = findViewById(R.id.step_line_2)
        txtStepName = findViewById(R.id.txt_step_name)

        btnBack = findViewById(R.id.btn_back_step)
        btnNext = findViewById(R.id.btn_next_step)

        inputFirstname = findViewById(R.id.input_firstname)
        inputMiddlename = findViewById(R.id.input_middlename)
        inputLastname = findViewById(R.id.input_lastname)
        inputStreet = findViewById(R.id.input_street)
        inputBarangay = findViewById(R.id.input_barangay)
        inputMunicipality = findViewById(R.id.input_municipality)
        inputProvince = findViewById(R.id.input_province)
        inputCountry = findViewById(R.id.input_country)
        inputContact = findViewById(R.id.input_contact)
        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        inputConfirmPassword = findViewById(R.id.input_confirm_password)

        pwRulesContainer = findViewById(R.id.pw_rules_container)
        pwRuleLength = findViewById(R.id.pw_rule_length)
        pwRuleUpper = findViewById(R.id.pw_rule_upper)
        pwRuleLower = findViewById(R.id.pw_rule_lower)
        pwRuleNumber = findViewById(R.id.pw_rule_number)
        pwRuleSpecial = findViewById(R.id.pw_rule_special)
    }

    private fun setupListeners() {
        // Back to landing
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        // Step navigation
        btnBack.setOnClickListener { goBack() }
        btnNext.setOnClickListener { goNext() }

        // Login link
        findViewById<TextView>(R.id.link_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Password strength watcher
        inputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val pw = s.toString()
                if (pw.isNotEmpty()) {
                    pwRulesContainer.visibility = View.VISIBLE
                    updatePasswordRules(pw)
                } else {
                    pwRulesContainer.visibility = View.GONE
                }
            }
        })
    }

    private fun goNext() {
        if (currentStep < 3) {
            currentStep++
            updateStepUI()
        } else {
            // Step 3 → Submit
            handleRegister()
        }
    }

    private fun goBack() {
        if (currentStep > 1) {
            currentStep--
            updateStepUI()
        }
    }

    private fun updateStepUI() {
        // Show/hide step containers
        step1.visibility = if (currentStep == 1) View.VISIBLE else View.GONE
        step2.visibility = if (currentStep == 2) View.VISIBLE else View.GONE
        step3.visibility = if (currentStep == 3) View.VISIBLE else View.GONE

        // Back button visibility
        btnBack.visibility = if (currentStep > 1) View.VISIBLE else View.GONE

        // Next button text
        btnNext.text = if (currentStep < 3) getString(R.string.btn_next) else getString(R.string.btn_register)

        // Step name
        txtStepName.text = stepNames[currentStep - 1]

        // Update step indicator circles + lines
        updateStepCircle(stepCircle1, 1)
        updateStepCircle(stepCircle2, 2)
        updateStepCircle(stepCircle3, 3)

        stepLine1.setBackgroundColor(
            getColor(if (currentStep > 1) R.color.step_line_completed else R.color.step_line_inactive)
        )
        stepLine2.setBackgroundColor(
            getColor(if (currentStep > 2) R.color.step_line_completed else R.color.step_line_inactive)
        )
    }

    private fun updateStepCircle(circle: TextView, stepNum: Int) {
        when {
            stepNum < currentStep -> {
                // Completed
                circle.setBackgroundResource(R.drawable.bg_step_completed)
                circle.text = "✓"
                circle.setTextColor(getColor(R.color.bg_dark_start))
            }
            stepNum == currentStep -> {
                // Active
                circle.setBackgroundResource(R.drawable.bg_step_active)
                circle.text = stepNum.toString()
                circle.setTextColor(getColor(R.color.text_white))
            }
            else -> {
                // Inactive
                circle.setBackgroundResource(R.drawable.bg_step_inactive)
                circle.text = stepNum.toString()
                circle.setTextColor(getColor(R.color.text_white_40))
            }
        }
    }

    private fun updatePasswordRules(pw: String) {
        updateRule(pwRuleLength, pw.length >= 8, getString(R.string.pw_length))
        updateRule(pwRuleUpper, pw.any { it.isUpperCase() }, getString(R.string.pw_uppercase))
        updateRule(pwRuleLower, pw.any { it.isLowerCase() }, getString(R.string.pw_lowercase))
        updateRule(pwRuleNumber, pw.any { it.isDigit() }, getString(R.string.pw_number))
        updateRule(pwRuleSpecial, pw.any { !it.isLetterOrDigit() }, getString(R.string.pw_special))
    }

    private fun updateRule(ruleView: TextView, passed: Boolean, label: String) {
        val icon = if (passed) "✓" else "✗"
        ruleView.text = "  $icon  $label"
        ruleView.setTextColor(getColor(if (passed) R.color.pw_rule_pass else R.color.pw_rule_fail))
    }

    private fun validatePassword(): Boolean {
        val pw = inputPassword.text.toString()
        val confirm = inputConfirmPassword.text.toString()

        val rules = listOf(
            pw.length >= 8,
            pw.any { it.isUpperCase() },
            pw.any { it.isLowerCase() },
            pw.any { it.isDigit() },
            pw.any { !it.isLetterOrDigit() }
        )

        if (rules.any { !it }) {
            ToastHelper.showError(this, getString(R.string.toast_password_weak))
            return false
        }

        if (pw != confirm) {
            ToastHelper.showError(this, getString(R.string.toast_password_mismatch))
            return false
        }

        return true
    }

    private fun handleRegister() {
        if (!validatePassword()) return

        val request = RegisterRequest(
            firstname = inputFirstname.text.toString().trim(),
            middlename = inputMiddlename.text.toString().trim(),
            lastname = inputLastname.text.toString().trim(),
            street = inputStreet.text.toString().trim(),
            barangay = inputBarangay.text.toString().trim(),
            municipality = inputMunicipality.text.toString().trim(),
            province = inputProvince.text.toString().trim(),
            country = inputCountry.text.toString().trim(),
            contactNumber = inputContact.text.toString().trim(),
            email = inputEmail.text.toString().trim(),
            password = inputPassword.text.toString()
        )

        btnNext.isEnabled = false
        btnNext.text = "Registering..."

        lifecycleScope.launch {
            val result = authRepository.register(request)
            result.onSuccess {
                // Navigate to login with success flag
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.putExtra("registered", true)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }.onFailure { e ->
                btnNext.isEnabled = true
                btnNext.text = getString(R.string.btn_register)
                ToastHelper.showError(this@RegisterActivity, e.message ?: getString(R.string.toast_register_error))
            }
        }
    }
}
