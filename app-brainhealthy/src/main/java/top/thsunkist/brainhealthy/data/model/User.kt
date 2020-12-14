package top.thsunkist.brainhealthy.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mooveit.library.Fakeit
import kotlinx.android.parcel.Parcelize
import top.thsunkist.brainhealthy.utilities.Debug
import top.thsunkist.brainhealthy.utilities.currentTimeSecs

@Parcelize
data class User(

	@SerializedName("avatar")
	var profilePictureUrl: String,

	@SerializedName("birthday")
	val birthday: Long?,

	@SerializedName("externalId")
	val externalId: String,

	@SerializedName("likeCount")
	val followerCount: Int = 0,

	@SerializedName("nick")
	var name: String,

	/** 性别 1.男 2.女*/
	@SerializedName("sex")
	val sex: Int,

	@SerializedName("description")
	var description: String?,

	/* toggle button the status 1:opened, 2:closed */
	@SerializedName("isShowAge")
	var showAge: Int?,

	/* company-user 1.yep 2.nop 是否工会成员, 只有是自己查自己，这个才有值，其它全为nil*/
	@SerializedName("isTradeUnion")
	var isUnion: Boolean?,

	/* this is message notification, mayby is belong to report? 1:开启，2：关闭*/
	@SerializedName("isPush")
	var isPush: Int? = 0,

	/* 工会成员，是否开启了，匹配通知，只有 isTradeUnion值为true，这里才有值 */
	@SerializedName("isTradeUnionMatchNotification")
	var isUnionPush: Boolean?,

	@SerializedName("isLike")
	var isLike: Boolean? = false,

	/* 1:edited, 2:wait for edit */
	@SerializedName("has_edited_country")
	val hasEditedCountry: Int,

	@SerializedName("unread_message_count")
	var unreadMessageCount: Int? = 0,

	@SerializedName("isSession")
	var isFreeMessage: Boolean? = false,

	/* VIP expire time  */
	@SerializedName("vip_expire_at")
	var vipExpireAt: Long? = 0,

	@SerializedName("defaultAvatar")
	val defaultAvatar: Boolean = false,
) : Parcelable {

	/* was compeleted basic-information */
	fun isCompleteMaterial(): Boolean {
		return (name.isBlank().not() && birthday != null)
	}

	companion object {
		fun fake(): User {
			return User(
				profilePictureUrl = Debug.images.random(),
				birthday = currentTimeSecs(),
				externalId = (100000..999999).random().toString(),
				followerCount = (100000..999999).random(),
				name = Fakeit.book().author(),
				sex = (1..2).random(),
				description = Fakeit.book().title(),
				showAge = (0..1).random(),
				hasEditedCountry = (0..1).random(),
				isUnion = null,
				isUnionPush = null,
			)
		}
	}

}