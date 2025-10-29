package com.example.smartstay.presentation.chat

import android.Manifest
import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.ChatAdapter
import com.example.smartstay.ChatViewModel
import com.example.smartstay.ChatViewModelFactory
import com.example.smartstay.ItemClickListener
import com.example.smartstay.R
import com.example.smartstay.databinding.FragmentChatBinding
import com.example.smartstay.model.ChatModel
import com.example.smartstay.model.ChatRequest
import com.example.smartstay.model.accommodation.AccommodationInfo
import com.example.smartstay.model.user.UserInfo
import com.example.smartstay.network.RetrofitInstance
import com.example.smartstay.presentation.AccommodationViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChatFragment : Fragment(R.layout.fragment_chat), ItemClickListener {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val chatViewModel: ChatViewModel by activityViewModels {
        ChatViewModelFactory(RetrofitInstance.backendNetworkService)
    }
    private val accommodationViewModel: AccommodationViewModel by activityViewModels()

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

//    private val userNickname: String? by lazy {
//        intent.getStringExtra("user_nickname")
//    }
//    private val userImage: String? by lazy {
//        intent.getStringExtra("user_image")
//    }
//    private val userId: String? by lazy {
//        intent.getStringExtra("user_id")
//    }
//    private val userInfo: UserInfo by lazy {
//        intent.getSerializableExtra("user_info") as UserInfo
//    }

    val testUserNickname = "테스터"
    val testUserImage =
        "https://img1.kakaocdn.net/thumb/R640x640.q70/?fname=https://t1.kakaocdn.net/account_images/default_profile.jpeg"
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

    private val recordBottomSheetDialog: RecordBottomSheetFragment by lazy {
        RecordBottomSheetFragment()
    }

    private val requestRecordPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                recordBottomSheetDialog.show(childFragmentManager, RecordBottomSheetFragment.TAG)
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    showPermissionRationalDialog()
                } else {
                    // 교육용 팝업을 이전에 봤는데도 불구하고 사용자가 허용을 하지 않은 경우
                    showPermissionSettingDialog()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() = with(binding) {
        linearLayoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(this@ChatFragment)
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
                    if (!isFilterInitialized) {
                        isFilterInitialized = true
                        lottiePointer.isVisible = false
                    }
                    drawerLayout.openDrawer(GravityCompat.END, true)
                    true
                }

                else -> false
            }
        }

        sivChatSend.setOnClickListener {

            if (!isChatInitialized) {
                lottieChatbot.isVisible = false
                tvInduceChat.isVisible = false
                isChatInitialized = true
            }

            val userMessage = etMessage.text.toString()
            if (userMessage.isBlank()) {
                Toast.makeText(context, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
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
//            processWithServer(
//                userId = testUserId,
//                userMessage = userMessage,
//                userInfo = testUserInfo,
//                keywords = keywordList
//            )

            // server 연결 X
            processWithoutServer()
        }
        sivChatRecord.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    recordBottomSheetDialog.show(
                        childFragmentManager,
                        RecordBottomSheetFragment.TAG
                    )
                }

                // 기존에 사용자가 권한 요청을 거부한 경우 → 교육용 팝업 띄우기
                shouldShowRequestPermissionRationale(
                    Manifest.permission.RECORD_AUDIO
                ) -> {
                    showPermissionRationalDialog()
                }

                // 권한을 처음 요청 받는 경우 → 시스템 팝업 띄우기
                else -> {
                    requestRecordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        }

        // side sheet (filter)
        sideSheetChipList.forEach { chip ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val checkedChip = Chip(context).apply {
                        text = chip.text
                        chipIcon = chip.chipIcon
                        chipIconTint =
                            ContextCompat.getColorStateList(context, R.color.primary)
                        chipBackgroundColor = ContextCompat.getColorStateList(
                            context,
                            R.color.background_gray_200
                        )
                        chipCornerRadius = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            16f,
                            resources.displayMetrics
                        )
                        chipStrokeColor =
                            ContextCompat.getColorStateList(context, R.color.primary)
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
        chatViewModel.chatbotResponse.observe(viewLifecycleOwner) { result ->
            result.onSuccess { chatbotMessage ->

                // 로딩 상태 제거 및 챗봇 응답 UI 갱신
                if (chatItemList.lastOrNull() is ChatModel.ChatBotLoading) {
                    chatItemList.removeAt(chatItemList.lastIndex)
                }
                chatItemList.add(chatbotMessage)
                chatAdapter.submitList(chatItemList.toList())

                // viewmodel 값 초기화
                if(chatbotMessage.accommodationInfo != null) {
                    accommodationViewModel.recommendAccommodationList = chatbotMessage.accommodationInfo
                }

                // 필터링 UI
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
                            if (chipKeyword == filterKeyword) {
                                chip.isChecked = true
                                if (chipgroupInput.children.any { (it as Chip).text == chip.text }) continue // 예외 처리
                                val filterChip = Chip(context).apply {
                                    text = chip.text
                                    id = chip.id
                                    chipIcon = chip.chipIcon
                                    chipIconTint =
                                        ContextCompat.getColorStateList(context, R.color.primary)
                                    chipBackgroundColor = ContextCompat.getColorStateList(
                                        context,
                                        R.color.background_gray_200
                                    )
                                    chipStrokeColor =
                                        ContextCompat.getColorStateList(context, R.color.primary)
                                    chipCornerRadius = TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        16f,
                                        resources.displayMetrics
                                    )
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
            }.onFailure { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
        chatViewModel.convertedVoiceRecord.observe(viewLifecycleOwner) { result ->
            result.onSuccess { convertedVoiceRecord ->
                recordBottomSheetDialog.dismiss()
                etMessage.setText(convertedVoiceRecord)
            }.onFailure { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 키보드 내리고 입력창 초기화하는 메소드
    private fun clearInputAndHideKeyboard() {
        binding.etMessage.setText("")
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun processWithoutServer() = with(binding) {
        val chatBotTestMessage: ChatModel.ChatBotMessage

        val lastUserMessage =
            chatItemList[chatItemList.lastIndex - 1] // chatItemList[chatItemList.lastIndex] = ChatModel.ChatBotLoading

        if ((lastUserMessage as ChatModel.UserMessage).message.contains("추천")) {
            chatBotTestMessage = ChatModel.ChatBotMessage(
                type = 1,
                message = "부산광역시 해운대구 위주로 한정해 숙소를 추천해드렸어요!",
                keywords = listOf("pet", "bar", "oceanView"),
                destinations = listOf(
                    Destination(
                        accommodation = Accommodation(
                            name = "르이데아호텔",
                            type = "호텔",
                            image = R.drawable.img_stay_1,
                            address = "부산광역시 남구 대연동 889-4",
                            latitude = 35.131891660643,
                            longitude = 129.092375826662,
                            minimumPrice = 85000,
                            averagePrice = 100000,
                            maximumPrice = 120000,
                            starRating = "1성",
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
                        attractions = listOf(
                            Attraction(
                                poiId = "1505596",
                                poiName = "신세계백화점센텀시티점",
                                category = "쇼핑",
                                lat = 35.168915568667,
                                lng = 129.129511134364,
                                congestionYn = "Y",
                                count = 54713,
                                distance = 5.324005522000573
                            ),
                            Attraction(
                                poiId = "8544989",
                                poiName = "유앤유키즈수영장",
                                category = "레저/스포츠",
                                lat = 35.165138482923,
                                lng = 129.169618814994,
                                congestionYn = "N",
                                count = 85727,
                                distance = 7.936141430578517
                            ),
                            Attraction(
                                poiId = "152111",
                                poiName = "광안리해수욕장",
                                category = "관광명소",
                                lat = 35.153167247931,
                                lng = 129.118651120125,
                                congestionYn = "Y",
                                count = 57850,
                                distance = 3.3620731996474036
                            )
                        )
                    ),
                    Destination(
                        accommodation = Accommodation(
                            name = "부산시티호텔",
                            type = "호텔",
                            image = R.drawable.img_stay_2,
                            address = "부산광역시 연제구 연산동 1372-25",
                            latitude = 35.182691453478,
                            longitude = 129.076210052287,
                            minimumPrice = 110000,
                            averagePrice = 130000,
                            maximumPrice = 160000,
                            starRating = "3성",
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
                        attractions = listOf(
                            Attraction(
                                poiId = "151890",
                                poiName = "세이브존해운대점",
                                category = "쇼핑",
                                lat = 35.162360957673,
                                lng = 129.161286230077,
                                congestionYn = "Y",
                                count = 36601,
                                distance = 8.056118401198521
                            ),
                            Attraction(
                                poiId = "10258740",
                                poiName = "J&P어린이수영장센텀마린점",
                                category = "레저/스포츠",
                                lat = 35.167860144471,
                                lng = 129.13156652273,
                                congestionYn = "N",
                                count = 59511,
                                distance = 5.294481989990581
                            ),
                            Attraction(
                                poiId = "11310286",
                                poiName = "마리앤쥬",
                                category = "관광명소",
                                lat = 35.15491667689,
                                lng = 129.062683721413,
                                congestionYn = "N",
                                count = 43133,
                                distance = 3.3239923303752144
                            )
                        )
                    ),
                    Destination(
                        accommodation = Accommodation(
                            name = "파크하얏트부산호텔",
                            type = "호텔",
                            image = R.drawable.img_stay_3,
                            address = "부산광역시 해운대구 우동 1409-3",
                            latitude = 35.156555916577,
                            longitude = 129.141871300461,
                            minimumPrice = 35000,
                            averagePrice = 50000,
                            maximumPrice = 60000,
                            starRating = "5성",
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
                        attractions = listOf(
                            Attraction(
                                poiId = "5871033",
                                poiName = "롯데마트동부산점",
                                category = "쇼핑",
                                lat = 35.192441291102,
                                lng = 129.212337067683,
                                congestionYn = "Y",
                                count = 52192,
                                distance = 7.5456553456485915
                            ),
                            Attraction(
                                poiId = "385086",
                                poiName = "부산요트경기장",
                                category = "레저/스포츠",
                                lat = 35.160472142576,
                                lng = 129.141149101421,
                                congestionYn = "N",
                                count = 76417,
                                distance = 0.4403647400694603
                            ),
                            Attraction(
                                poiId = "2788539",
                                poiName = "부산시민공원",
                                category = "관광명소",
                                lat = 35.168165164466,
                                lng = 129.057072964066,
                                congestionYn = "N",
                                count = 54838,
                                distance = 7.815510402362057
                            )
                        )
                    ),
                    Destination(
                        accommodation = Accommodation(
                            name = "토요코인호텔부산서면점",
                            type = "호텔",
                            image = R.drawable.img_stay_4,
                            address = "부산광역시 부산진구 전포동 666-8",
                            latitude = 35.157999676181,
                            longitude = 129.064044684141,
                            minimumPrice = 90000,
                            averagePrice = 110000,
                            maximumPrice = 140000,
                            starRating = "2성",
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
                        attractions = listOf(
                            Attraction(
                                poiId = "1089861",
                                poiName = "쥬디스태화신관",
                                category = "쇼핑",
                                lat = 35.155111083684,
                                lng = 129.060295037245,
                                congestionYn = "N",
                                count = 28204,
                                distance = 0.46834618308152354
                            ),
                            Attraction(
                                poiId = "11075630",
                                poiName = "클럽디오아시스",
                                category = "레저/스포츠",
                                lat = 35.160166808992,
                                lng = 129.16889670523,
                                congestionYn = "N",
                                count = 67018,
                                distance = 9.534486161354401
                            ),
                            Attraction(
                                poiId = "1161965",
                                poiName = "APEC나루공원",
                                category = "관광명소",
                                lat = 35.171720776709,
                                lng = 129.124289335682,
                                congestionYn = "N",
                                count = 51865,
                                distance = 5.6846273488434065
                            )
                        )
                    ),
                    Destination(
                        accommodation = Accommodation(
                            name = "호텔스미스부산남구청점",
                            type = "호텔",
                            image = R.drawable.img_stay_5,
                            address = "부산광역시 남구 대연동 1767-4",
                            latitude = 35.135224573171,
                            longitude = 129.084931992772,
                            minimumPrice = 130000,
                            averagePrice = 150000,
                            maximumPrice = 180000,
                            starRating = "2성",
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
                        ),
                        attractions = listOf(
                            Attraction(
                                poiId = "1505593",
                                poiName = "롯데백화점센텀시티점",
                                category = "쇼핑",
                                lat = 35.169693272659,
                                lng = 129.1313998521,
                                congestionYn = "Y",
                                count = 37341,
                                distance = 5.703898850340064
                            ),
                            Attraction(
                                poiId = "5691424",
                                poiName = "신세계센텀시티아이스링크",
                                category = "레저/스포츠",
                                lat = 35.168915569418,
                                lng = 129.129622235866,
                                congestionYn = "N",
                                count = 80892,
                                distance = 5.52630141650139
                            ),
                            Attraction(
                                poiId = "2281094",
                                poiName = "센텀시티스파랜드",
                                category = "관광명소",
                                lat = 35.168915569418,
                                lng = 129.129622235866,
                                congestionYn = "N",
                                count = 80756,
                                distance = 5.52630141650139
                            )
                        )
                    ),
                )
            )
        } else {
            chatBotTestMessage = ChatModel.ChatBotMessage(
                type = 0,
                message = "안녕하세요! 응답 테스트 중입니다. '추천'이 들어간 채팅을 임의로 넣으시면 테스트용 숙소가 추천됩니다.",
                keywords = null,
                destinations = null
            )
        }

        if (chatItemList.last() is ChatModel.ChatBotLoading) chatItemList.removeAt(chatItemList.lastIndex)
        chatItemList.add(chatBotTestMessage)
        chatAdapter.submitList(chatItemList.toList())

        // viewmodel 값 설정
        if(chatBotTestMessage.accommodationInfo != null) {
            accommodationViewModel.recommendAccommodationList = chatBotTestMessage.accommodationInfo
        }

        if (chatBotTestMessage.keywords != null) {

            // 기존 필터 초기화
            chipgroupInput.removeAllViews()
            val chipGroupFilter = sideSheet.findViewById<ChipGroup>(R.id.chipgroup_filter)
            for (i in 0 until chipGroupFilter.childCount) {
                val chip = chipGroupFilter.getChildAt(i) as Chip
                chip.isChecked = false
            }

            // 챗봇 응답 필터링 자동 켜기
            chatBotTestMessage.keywords.forEach { keyword ->
                for (i in 0 until chipGroupFilter.childCount) {
                    val chip = chipGroupFilter.getChildAt(i) as Chip
                    val chipFilter = resources.getResourceEntryName(chip.id)
                    if (chipFilter == keyword) {
                        chip.isChecked = true
                        if (chipgroupInput.children.any { (it as Chip).text == chip.text }) continue // 예외 처리
                        val filteredChip = Chip(context).apply {
                            text = chip.text
                            id = chip.id
                            chipIcon = chip.chipIcon
                            chipIconTint = ContextCompat.getColorStateList(context, R.color.primary)
                            chipBackgroundColor = ContextCompat.getColorStateList(
                                context,
                                R.color.background_gray_200
                            )
                            chipStrokeColor =
                                ContextCompat.getColorStateList(context, R.color.primary)
                            chipCornerRadius = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                16f,
                                resources.displayMetrics
                            )
                            isCloseIconVisible = true
                            setOnCloseIconClickListener {
                                chipgroupInput.removeView(this@apply)
                                chip.isChecked = false
                            }
                        }
                        chipgroupInput.addView(filteredChip)
                    }
                }
            }
        }
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
        AlertDialog.Builder(context)
            .setMessage("녹음 권한을 켜주셔야 해당 기능을 이용할 수 있습니다.")
            .setPositiveButton("권한 허용하기") { dialog, position ->
                requestRecordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            .setNegativeButton("취소") { dialog, position ->
                dialog.cancel()
            }
            .show()
    }

    // 교육용 팝업 거절 이후 직접 설정 화면으로 유도하는 다이얼로그
    private fun showPermissionSettingDialog() {
        AlertDialog.Builder(context)
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
            data = Uri.fromParts("package", context?.packageName, null) // 우리 앱 패키지의 설정 디테일로 이동
        }
        startActivity(intent)
    }

    override fun onNavigateToDetail(item: AccommodationInfo) {
        val action = ChatFragmentDirections.actionChatFragmentToStayDetailFragment(item)
        findNavController().navigate(action)
    }

}