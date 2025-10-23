package com.example.smartstay

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ActivityChatBinding
import com.example.smartstay.model.AccommodationInfo
import com.example.smartstay.model.ChatModel
import com.example.smartstay.model.UserInput
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.smartstay.model.ChatRequest
import com.example.smartstay.model.accommodation.AccommodationInfo
import com.example.smartstay.model.user.UserInfo
import com.example.smartstay.network.RetrofitInstance

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val chatViewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(RetrofitInstance.networkService)
    }

    private lateinit var chatBotMessage: ChatModel.ChatBotMessage

    private val sideSheet by lazy {
        binding.navigationView.getHeaderView(0)
    }
    private val sideSheetChipList by lazy {
        listOf(
            sideSheet.findViewById<Chip>(R.id.pet),
            sideSheet.findViewById<Chip>(R.id.restaurant),
            sideSheet.findViewById<Chip>(R.id.bar),
            sideSheet.findViewById<Chip>(R.id.cafe),
            sideSheet.findViewById<Chip>(R.id.fitnessCenter),
            sideSheet.findViewById<Chip>(R.id.swimmingPool),
            sideSheet.findViewById<Chip>(R.id.spa),
            sideSheet.findViewById<Chip>(R.id.sauna),
            sideSheet.findViewById<Chip>(R.id.receptionHall),
            sideSheet.findViewById<Chip>(R.id.businessCenter),
            sideSheet.findViewById<Chip>(R.id.oceanView)
        )
    }

    private val userNickname: String? by lazy {
        intent.getStringExtra("user_nickname")
    }
    private val userImage: String? by lazy {
        intent.getStringExtra("user_image")
    }
    private val userId: String? by lazy {
        intent.getStringExtra("user_id")
    }
    private val userInfo: UserInfo by lazy {
        intent.getSerializableExtra("user_info") as UserInfo
    }

    val testUserNickname = "테스터"
    val testUserImage = "https://img1.kakaocdn.net/thumb/R640x640.q70/?fname=https://t1.kakaocdn.net/account_images/default_profile.jpeg"
    val testUserId = "4256657082"
    val testUserInfo = UserInfo(
        genderCode = "M",
        age = 38,
        jobType = "기술직",
        marriageType = "기혼",
        childrenType = "자녀 있음",
        familyType = "4",
        incomePerMember = 330.0f,
        isCompanionExist = "Y",
        companionType = "배우자"
    )

    private val chatItemList = mutableListOf<ChatModel>()

    private var isFilterInitialized: Boolean = false
    private var isChatInitialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() = with(binding) {
        linearLayoutManager = LinearLayoutManager(this@ChatActivity)
        chatAdapter = ChatAdapter(this@ChatActivity)
        with(recyclerviewChat) {
            layoutManager = linearLayoutManager
            adapter = chatAdapter
        }
    }

    private fun initListeners() = with(binding) {
        // chat (main)
        toolbarChat.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.toolbar_filtering -> {
                    if(!isFilterInitialized) {
                        isFilterInitialized = true
                        lottiePointer.isVisible = false
                    }
                    drawerLayout.openDrawer(GravityCompat.END, true)
                    true
                }
                else -> false
            }
        }
        cvRecord.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this@ChatActivity,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 녹음 권한이 부여된 경우
                    // 실제 녹음 시작하기
                }

                // 기존에 사용자가 권한 요청을 거부한 경우 → 교육용 팝업 띄우기
                ActivityCompat.shouldShowRequestPermissionRationale(this@ChatActivity, Manifest.permission.RECORD_AUDIO) -> {
                    showPermissionRationalDialog()
                }

                // 권한을 처음 요청 받는 경우 → 시스템 팝업 띄우기
                else -> {
                    ActivityCompat.requestPermissions(this@ChatActivity,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        REQUEST_RECORD_AUDIO_CODE)
                }
            }
        }
        cvSend.setOnClickListener {

            if(!isChatInitialized) {
                lottieChatbot.isVisible = false
                tvInduceChat.isVisible = false
                isChatInitialized = true
            }

            val userMessage = etMessage.text.toString()
            if (userMessage.isBlank()) {
                Toast.makeText(this@ChatActivity, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val keywordList = mutableListOf<String>()
            sideSheetChipList.forEach { chip ->
                if (chip.isChecked) {
                    val keyword = resources.getResourceEntryName(chip.id)
                    keywordList.add(keyword)
                }
            }

            // chat UI
            chatItemList.add(
                ChatModel.UserMessage(
                    profile = testUserImage,
                    nickname = testUserNickname,
                    message = userMessage,
                    keywords = keywordList
                )
            )

            chatItemList.add(
                ChatModel.ChatBotLoading
            )

            chatAdapter.submitList(chatItemList.toList())

            clearInputAndHideKeyboard()

            // server 연결 O
            processWithServer(
                userId = testUserId,
                userMessage = userMessage,
                userInfo = testUserInfo,
                keywords = keywordList
            )

            // server 연결 X
            // processWithoutServer()
        }

        // side sheet (filter)
        sideSheetChipList.forEach { chip ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val checkedChip = Chip(this@ChatActivity).apply {
                        text = chip.text
                        chipIcon = chip.chipIcon
                        chipIconTint = ContextCompat.getColorStateList(this@ChatActivity, R.color.primary)
                        chipBackgroundColor = ContextCompat.getColorStateList(this@ChatActivity, R.color.background_chip)
                        chipCornerRadius = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            16f,
                            resources.displayMetrics
                        )
                        chipStrokeColor = ContextCompat.getColorStateList(this@ChatActivity, R.color.primary)
                        isCloseIconVisible = true
                        setOnCloseIconClickListener {
                            chipgroupInput.removeView(this)
                            chip.isChecked = false
                        }
                    }
                    chipgroupInput.addView(checkedChip)
                } else {
                    val uncheckedChip = chipgroupInput.children.filterIsInstance<Chip>()
                        .firstOrNull { it.text == chip.text }
                    chipgroupInput.removeView(uncheckedChip)
                }
            }
        }
        sideSheet.findViewById<LinearLayout>(R.id.ll_back_to_chat).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END, true)
        }
    }

    private fun initObservers() = with(binding) {
        // 아이템이 들어올 때 옵져빙하여 가장 마지막 위치로 이동하기
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                linearLayoutManager.smoothScrollToPosition(
                    recyclerviewChat,
                    null,
                    chatAdapter.itemCount - 1
                )
            }
        })
        chatViewModel.chatbotResponse.observe(this@ChatActivity) { result ->
            result.onSuccess { chatbotMessage ->
                // 기존 필터링 초기화 및 챗봇 응답 필터링 자동으로 켜기
                // TODO: null / emptyList() 둘 중 하나 선택하기
                if (chatbotMessage.keywords != null) {

                    // 기존 필터 초기화
                    chipgroupInput.removeAllViews()

                    val chipGroupFilter = sideSheet.findViewById<ChipGroup>(R.id.chipgroup_filter)
                    for (i in 0 until chipGroupFilter.childCount) {
                        val chip = chipGroupFilter.getChildAt(i) as Chip
                        chip.isChecked = false
                    }

                    chatbotMessage.keywords.forEach { filterKeyword ->
                        for (i in 0 until chipGroupFilter.childCount) {
                            val chip = chipGroupFilter.getChildAt(i) as Chip
                            val chipKeyword = resources.getResourceEntryName(chip.id)
                            if(chipKeyword == filterKeyword) {
                                chip.isChecked = true
                                val filterChip = chip.apply {
                                    chipIconTint =
                                        ContextCompat.getColorStateList(this@ChatActivity, R.color.primary)
                                    chipBackgroundColor = ContextCompat.getColorStateList(
                                        this@ChatActivity,
                                        R.color.background_chip
                                    )
                                    chipStrokeColor =
                                        ContextCompat.getColorStateList(this@ChatActivity, R.color.primary)
                                    isCloseIconVisible = true
                                    setOnCloseIconClickListener {
                                        chipgroupInput.removeView(this@apply)
                                        chip.isChecked = false
                                    }
                                }
                                chipgroupInput.addView(filterChip)
                            }
                        }
                    }
                }

                // 로딩 상태 제거 및 챗봇 응답 UI 갱신
                if (chatItemList.lastOrNull() is ChatModel.ChatBotLoading) chatItemList.removeAt(chatItemList.lastIndex)
                chatItemList.add(chatbotMessage)
                chatAdapter.submitList(chatItemList.toList())
            }.onFailure { error ->
                Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 권한 요청했을 때 시스템 팝업이 뜸
     * 권한이 허용되지 않을 수도 있음
     * 서로 다른 권한을 요청하더라도 똑같은 콜백이 호출되기 때문에, 요청 코드(REQUEST_CODE)로 권한을 구분해야한다.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isAudioRecordPermissionGranted: Boolean = (requestCode == REQUEST_RECORD_AUDIO_CODE) && (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED)
        if(isAudioRecordPermissionGranted) {
            Toast.makeText(this, "녹음 작업을 시작합니다.", Toast.LENGTH_SHORT).show()
        } else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@ChatActivity, Manifest.permission.RECORD_AUDIO)) {
                showPermissionRationalDialog()
            } else {
                // 교육용 팝업을 이전에 봤는데도 불구하고 사용자가 허용을 하지 않은 경우
                showPermissionSettingDialog()
            }
        }
    }

    // // 키보드 내리고 입력창 초기화하는 메소드
    private fun clearInputAndHideKeyboard() {
        binding.etMessage.setText("")
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this@ChatActivity.currentFocus
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun processWithoutServer() {

        val chatbotRandomMessage = "안녕하세요! 서버를 연결하지 않은 테스트용 응답입니다."

        val lastUserMessage = chatItemList.last()

        if ((lastUserMessage as ChatModel.UserMessage).message.contains("추천")) {
            chatBotMessage = ChatModel.ChatBotMessage(
                type = 1,
                message = "숙소를 추천해드렸어요!",
                keywords = listOf("반려동물", "레스토랑", "바"),
                accommodationInfo = listOf(
                    AccommodationInfo(
                        name = "서울 센트럴 호텔",
                        type = "호텔",
                        image = R.drawable.img_stay_1,
                        address = "서울특별시 중구 세종대로 110",
                        latitude = 37.5665f,
                        longitude = 126.9780f,
                        minimumPrice = 85000,
                        averagePrice = 100000,
                        maximumPrice = 120000,
                        starRating = "3성",
                        finalRating = 4,
                        isPetAvailable = "N",
                        isRestaurantExist = "Y",
                        isBarExist = "Y",
                        isCafeExist = "Y",
                        isFitnessCenterExist = "N",
                        isSwimmingPoolExist = "N",
                        isSpaExist = "N",
                        isSaunaExist = "N",
                        isReceptionCenterExist = "Y",
                        isBusinessCenterExist = "Y",
                        isOceanViewExist = "N"
                    ),
                    AccommodationInfo(
                        name = "강남 프리미엄 레지던스",
                        type = "레지던스",
                        image = R.drawable.img_stay_2,
                        address = "서울특별시 강남구 테헤란로 212",
                        latitude = 37.5013f,
                        longitude = 127.0396f,
                        minimumPrice = 110000,
                        averagePrice = 130000,
                        maximumPrice = 160000,
                        starRating = "4성",
                        finalRating = 5,
                        isPetAvailable = "Y",
                        isRestaurantExist = "Y",
                        isBarExist = "N",
                        isCafeExist = "Y",
                        isFitnessCenterExist = "Y",
                        isSwimmingPoolExist = "N",
                        isSpaExist = "N",
                        isSaunaExist = "N",
                        isReceptionCenterExist = "N",
                        isBusinessCenterExist = "Y",
                        isOceanViewExist = "N"
                    ),
                    AccommodationInfo(
                        name = "홍대 스타일 게스트하우스",
                        type = "게스트하우스",
                        image = R.drawable.img_stay_3,
                        address = "서울특별시 마포구 와우산로 29",
                        latitude = 37.5561f,
                        longitude = 126.9229f,
                        minimumPrice = 35000,
                        averagePrice = 50000,
                        maximumPrice = 60000,
                        starRating = "2성",
                        finalRating = 3,
                        isPetAvailable = "N",
                        isRestaurantExist = "N",
                        isBarExist = "Y",
                        isCafeExist = "Y",
                        isFitnessCenterExist = "N",
                        isSwimmingPoolExist = "N",
                        isSpaExist = "N",
                        isSaunaExist = "N",
                        isReceptionCenterExist = "N",
                        isBusinessCenterExist = "N",
                        isOceanViewExist = "N"
                    ),
                    AccommodationInfo(
                        name = "이태원 뷰 호텔",
                        type = "호텔",
                        image = R.drawable.img_stay_4,
                        address = "서울특별시 용산구 이태원로 188",
                        latitude = 37.5349f,
                        longitude = 126.9948f,
                        minimumPrice = 90000,
                        averagePrice = 110000,
                        maximumPrice = 140000,
                        starRating = "3성",
                        finalRating = 4,
                        isPetAvailable = "Y",
                        isRestaurantExist = "Y",
                        isBarExist = "Y",
                        isCafeExist = "Y",
                        isFitnessCenterExist = "Y",
                        isSwimmingPoolExist = "N",
                        isSpaExist = "N",
                        isSaunaExist = "N",
                        isReceptionCenterExist = "Y",
                        isBusinessCenterExist = "N",
                        isOceanViewExist = "N"
                    ),
                    AccommodationInfo(
                        name = "한강 리버뷰 호텔",
                        type = "호텔",
                        image = R.drawable.img_stay_5,
                        address = "서울특별시 영등포구 여의대로 24",
                        latitude = 37.5219f,
                        longitude = 126.9246f,
                        minimumPrice = 130000,
                        averagePrice = 150000,
                        maximumPrice = 180000,
                        starRating = "5성",
                        finalRating = 5,
                        isPetAvailable = "N",
                        isRestaurantExist = "Y",
                        isBarExist = "Y",
                        isCafeExist = "Y",
                        isFitnessCenterExist = "Y",
                        isSwimmingPoolExist = "Y",
                        isSpaExist = "Y",
                        isSaunaExist = "Y",
                        isReceptionCenterExist = "Y",
                        isBusinessCenterExist = "Y",
                        isOceanViewExist = "Y"
                    )
                )
            )
        } else {
            chatBotMessage = ChatModel.ChatBotMessage(
                type = 0,
                message = chatbotRandomMessage,
                accommodationInfo = null
            )
        }

        // 필터링 자동 켜기
        if (chatBotMessage.keywords != null) {

            val sideSheet = binding.sideSheet.getHeaderView(0)
            val chipGroupFilter = sideSheet.findViewById<ChipGroup>(R.id.chipgroup_filter)

            // 기존 필터 초기화
            binding.chipgroupInput.removeAllViews()
            for(i in 0 until chipGroupFilter.childCount) {
                val chip = chipGroupFilter.getChildAt(i) as Chip
                chip.isChecked = false
            }

            chatBotMessage.keywords?.let { keywords ->
                keywords.forEach { keyword ->
                    for (i in 0 until chipGroupFilter.childCount) {
                        val chip = chipGroupFilter.getChildAt(i) as Chip
                        if (chip.text == keyword) {
                            chip.isChecked = true
                        }
                    }
                }
            }
        }

        chatItemList.add(chatBotMessage)
        chatAdapter.submitList(chatItemList.toList())
    }

    private fun processWithServer(
        userId: String,
        userMessage: String,
        keywords: List<String>,
        userInfo: UserInfo
    ) {
        chatViewModel.postSocialChat(
            chatRequest =
                ChatRequest(
                    userId = userId,
                    message = userMessage,
                    keywords = keywords,
                    userInfo = userInfo
                )
        )
    }

    // 권한이 왜 필요한 지 안내하는 다이얼로그 (교육용 팝업)
    private fun showPermissionRationalDialog() {
        AlertDialog.Builder(this)
            .setMessage("녹음 권한을 켜주셔야 해당 기능을 이용할 수 있습니다.")
            .setPositiveButton("권한 허용하기") { dialog, position ->
                ActivityCompat.requestPermissions(this@ChatActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO_CODE)
            }
            .setNegativeButton("취소") { dialog, position ->
                dialog.cancel()
            }
            .show()
    }

    // 교육용 팝업 거절 이후 직접 설정 화면으로 유도하는 다이얼로그
    private fun showPermissionSettingDialog() {
        AlertDialog.Builder(this)
            .setMessage("녹음 권한을 켜주셔야 해당 기능을 이용할 수 있습니다. 앱 설정 화면으로 진입하셔서 권한을 켜주세요.")
            .setPositiveButton("설정 화면 가기") { dialog, position ->
                navigateToSystemSetting()
            }
            .setNegativeButton("취소") { dialog, position ->
                dialog.cancel()
            }
            .show()
    }

    // 시스템 설정 화면으로 이동하는 메소드
    private fun navigateToSystemSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null) // 우리 앱 패키지의 설정 디테일로 이동
        }
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_CODE = 200
    }
}