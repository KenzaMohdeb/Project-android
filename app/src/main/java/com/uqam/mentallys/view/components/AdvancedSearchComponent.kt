package com.uqam.mentallys.view.components

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.lifecycle.MutableLiveData
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentComponentAdvancedSearchBinding
import kotlin.properties.Delegates


class AdvancedSearchComponent : LinearLayout {
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : super(context, attrs, defStyleAttr) {
        abstractConstructor(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        abstractConstructor(context, attrs)
    }

    private var hintMarginBottom by Delegates.notNull<Int>()
    private var hintMarginLeft by Delegates.notNull<Int>()
    private var imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
    private lateinit var binding: FragmentComponentAdvancedSearchBinding
    private var wasKeyboardVisible: Boolean = false
    private var isKeyboardVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    private var title: String = ""
    private var subtitle: String = ""
    private var isFilterButtonShown: MutableLiveData<Boolean> = MutableLiveData(true)
    var onTextUpdateCallBack: ((text: String) -> Unit)? = null
    var onFilterButtonClickCallBack: (() -> Unit)? = null
    var onSearchActiveCallBack: (() -> Unit)? = null
    var onSearchInactiveCallBack: (() -> Unit)? = null
    var onSearchBackPressedCallBack: (() -> Unit)? = null
    var onEnterPressedCallback: ((view: View, i: Int, keyEvent: KeyEvent) -> Unit)? = null
    var ignoreStateChange: Boolean = false


    private fun abstractConstructor(context: Context, attrs: AttributeSet?) {
        isFocusable = true
        requestFocus()
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                R.styleable.advanced_search_component_attributes, 0, 0)
            this.title =
                resources.getString(typedArray.getResourceId(R.styleable.advanced_search_component_attributes_advanced_search_component_title,
                    R.string.empty))
            this.subtitle =
                resources.getString(typedArray.getResourceId(R.styleable.advanced_search_component_attributes_advanced_search_component_subtitle,
                    R.string.empty))
            this.isFilterButtonShown.value =
                typedArray.getBoolean(R.styleable.advanced_search_component_attributes_advanced_search_component_is_filter_button_shown,
                    false)
        }
        LayoutInflater.from(context)
            .inflate(R.layout.fragment_component_advanced_search, this, true)
        orientation = VERTICAL
        binding = FragmentComponentAdvancedSearchBinding.bind(rootView)
        binding.apply {
            // Set filter as inactive by default
            setFilterInactive()
            // Retrieve flip flop property
            hintMarginBottom = searchHint.marginBottom
            hintMarginLeft = searchHint.marginLeft
            // Set component state
            searchFragmentHelper.text = title
            searchHint.text = subtitle
            if (!isFilterButtonShown.value!!) {
                searchFragmentButtonFilter.visibility = View.GONE
            } else {
                searchFragmentButtonFilter.visibility = View.VISIBLE
            }
            // Add event listener on the input box to refresh the ui and update the search
            searchInputBox.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    // No need to update anything
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // No need to update anything
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setViewFromState()
                    onTextUpdateCallBack?.let { it(p0.toString()) }
                }
            })
            // Set filter click listener
            searchFragmentButtonFilter.setOnClickListener {
                onFilterButtonClickCallBack?.let { it() }
            }
            // Set on key listener
            searchInputBox.setOnKeyListener { view, i, keyEvent ->
                onEnterPressedCallback?.let { it(view, i, keyEvent) }
                false
            }
            // Updater the view when the input box focus status change
            searchInputBox.setOnFocusChangeListener { _, _ ->
                setViewFromState()
            }
            // Listen on keyboard state change
            // https://stackoverflow.com/questions/42892419/android-detect-soft-keyboard-closing
            rootView.viewTreeObserver.addOnGlobalLayoutListener {
                isKeyboardVisible.value = isKeyBoardUp()
            }
            // Update state based on the keyboard states stack
            isKeyboardVisible.observeForever {
                if (wasKeyboardVisible && !it) {
                    searchInputBox.clearFocus()
                    setViewFromState()
                }
                wasKeyboardVisible = it
            }
        }
    }

    private fun isKeyBoardUp(): Boolean {
        val measureRect =
            Rect() //you should cache this, onGlobalLayout can get called often
        rootView.getWindowVisibleDisplayFrame(measureRect)
        // measureRect.bottom is the position above soft keypad
        val keypadHeight: Int = rootView.rootView.height - measureRect.bottom
        return keypadHeight > 100
    }

    private fun setViewFromState() {
        if (!ignoreStateChange) {
            if (binding.searchInputBox.hasFocus() && binding.searchInputBox.text.toString() == "") setStateEmpty()
            else if (binding.searchInputBox.hasFocus() && binding.searchInputBox.text.toString() != "") setStateFilling()
            else if (!binding.searchInputBox.hasFocus() && binding.searchInputBox.text.toString() == "") setStateIdle()
            else if (!binding.searchInputBox.hasFocus() && binding.searchInputBox.text.toString() != "") setStateOutOfFocus()
        }
    }

    fun setStateIdle() {
        onSearchInactiveCallBack?.let { it() }
        binding.apply {
            searchIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_search_24))
            searchFragmentHelper.visibility = View.VISIBLE
            searchHint.visibility = View.VISIBLE
            val lp = searchHint.layoutParams as ConstraintLayout.LayoutParams
            searchHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            lp.setMargins(hintMarginLeft, 0, 0, hintMarginBottom)
            imm.hideSoftInputFromWindow(rootView?.windowToken, 0)
            searchIcon.setOnClickListener {

                searchInputBox.requestFocus()
                imm.showSoftInput(searchInputBox, 0)
            }
        }
    }

    fun setStateEmpty() {
        onSearchActiveCallBack?.let { it() }
        binding.apply {
            searchIcon.setImageDrawable(context.getDrawable(R.drawable.ic_left_arrow))
            searchFragmentHelper.visibility = View.GONE
            searchHint.visibility = View.VISIBLE
            val lp = searchHint.layoutParams as ConstraintLayout.LayoutParams
            searchHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            lp.setMargins(hintMarginLeft, 0, 0, 0)
            searchIcon.setOnClickListener {
                onSearchBackPressedCallBack?.let { it1 -> it1() }
                searchInputBox.clearFocus()
                searchFragmentDummy.requestFocus()
                //this@AdvancedSearchComponent.requestFocus()
            }
        }
    }

    fun setStateFilling() {
        onSearchActiveCallBack?.let { it() }
        binding.apply {
            searchInputBox.requestFocus()
            imm.showSoftInput(rootView, 0)
            searchIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_close_24))
            searchFragmentHelper.visibility = View.GONE
            searchHint.visibility = View.GONE
            searchIcon.setOnClickListener {
                onSearchBackPressedCallBack?.let { it1 -> it1() }
                searchInputBox.setText("")
            }
        }
    }

    fun setStateOutOfFocus() {
        onSearchInactiveCallBack?.let { it() }
        binding.apply {
            searchIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_close_24))
            searchFragmentHelper.visibility = View.GONE
            searchHint.visibility = View.GONE
            imm.hideSoftInputFromWindow(rootView?.windowToken, 0)
            searchInputBox.clearFocus()
            searchFragmentDummy.requestFocus()
            searchIcon.setOnClickListener {
                onSearchBackPressedCallBack?.let { it1 -> it1() }
                searchInputBox.setText("")
                imm.showSoftInput(searchInputBox, 0)
            }

        }
    }

    fun setFilterActive() {
        binding.apply {
            searchFragmentButtonFilterBorder.visibility = View.VISIBLE
            searchFragmentButtonFilterDot.visibility = View.VISIBLE
        }
    }

    fun setFilterInactive() {
        binding.apply {
            searchFragmentButtonFilterBorder.visibility = View.GONE
            searchFragmentButtonFilterDot.visibility = View.GONE
        }
    }

    fun keepFocus() {
        binding.searchInputBox.clearFocus()
        binding.searchInputBox.requestFocus()
        setViewFromState()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.searchInputBox.text.toString() != "") {
            binding.searchInputBox.setText("")
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun setInputText(text: String) {
        binding.searchInputBox.setText(text)
        onTextUpdateCallBack?.let { it(binding.searchInputBox.text.toString()) }
    }

}