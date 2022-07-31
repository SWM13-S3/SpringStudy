# BaseEntity

- 데이터의 생성 시간
- 마지막으로 수정된 시간
- 생성한 사람
- 마지막으로 수정한 사람

에 대한 정보(컬럼)를 자동으로 만들어주는 추상 엔티티

~~~java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createDate;

	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
}
~~~

- @MappedSuperClass<br>
이 클래스를 상속받는 Entity에 이 BaseEntity의 컬럼들이 자동으로 등록시키겠다는 의미
- @EntityListeners(AuditingEntityListener.class)<br>
자동으로 값을 mapping시키는 auditing 기능을 사용하겠다는 의미

# 빌더 패턴(Builder Pattern)

생성자 패턴 중 하나<br>
코드의 변경 과정을 하나씩 살펴보자

## 1. 생성자를 이용한 인스턴스 생성

~~~java
// User.java
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	private String name;
	private String phoneNumber;
	private boolean pushAlarmAllow;

	private String profileImage;

	private Gender gender;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Oauth> oauths;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserTaste> userTastes;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<CustomIngredient> customIngredients;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserIngredient> userIngredients;

	public static User create(
		String name,
		String phoneNumber,
		boolean pushAlarmAllow,
		String profileImage,
		Gender gender,
		List<Oauth> oauths,
		List<UserTaste> userTastes,
		List<CustomIngredient> customIngredients,
		List<UserIngredient> userIngredients
	) {
		return new User(
			null,
			name,
			phoneNumber,
			pushAlarmAllow,
			profileImage,
			gender,
			oauths,
			userTastes,
			customIngredients,
			userIngredients
		);
	}
}
~~~

~~~java
// UserRepositoryTest.java
@Test
@Transactional
@DisplayName("유저 추가 테스트")
public void createTest() {
    User user = User.create(
        "test",
        "010-1234-5678",
        false,
        "src/test_profile.jpg",
        Gender.M,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>()
    );
    User newUser = userRepository.save(user);

    Optional<User> findUser = userRepository.findById(newUser.getId());

    Assertions.assertFalse(newUser.equals(findUser));
}
~~~
- 매우 조잡한 코드
- 각각의 인자가 무엇을 의미하는지 모름
- new ArrayList<>와 같이 빈 필드도 일일이 만들어줘야함

## 빌더 패턴 적용

### 빌더 패턴
인스턴스 생성을 담당하는 Builder라는 것을 Entity 내부 클래스로 가진다.

~~~java
// User.java
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    // User Entity 내부 컬럼 여러개 주저리주저리
    // 있다치고

    private User(UserBuilder userBuilder){
		this.name = userBuilder.phoneNumber;
		this.phoneNumber = userBuilder.phoneNumber;
		this.pushAlarmAllow = userBuilder.pushAlarmAllow;
		this.profileImage = userBuilder.profileImage;
		this.gender = userBuilder.gender;
		this.oauths = userBuilder.oauths;
		this.userTastes = userBuilder.userTastes;
	}

    public static class UserBuilder {
		private String name;
		private String phoneNumber;
		private boolean pushAlarmAllow;
		private String profileImage;
		private Gender gender;
		private List<Oauth> oauths;
		private List<UserTaste> userTastes;

        // 기본적으로 User를 만들 때 꼭 필요한 정보도 지정할 수 있음
		public UserBuilder() {
		}

		public UserBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public UserBuilder setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public UserBuilder setPushAlarmAllow(boolean pushAlarmAllow){
			this.pushAlarmAllow = pushAlarmAllow;
			return this;
		}

		public UserBuilder setProfileImage(String profileImage){
			this.profileImage = profileImage;
			return this;
		}

		public UserBuilder setGender(Gender gender){
			this.gender = gender;
			return this;
		}

		public UserBuilder setOauths(List<Oauth> oauths){
			this.oauths = oauths;
			return this;
		}

		public UserBuilder setUserTastes(List<UserTaste> userTastes) {
			this.userTastes = userTastes;
			return this;
		}

		public User build(){
			return new User(this);
		}
	}
}
~~~

~~~java
// UserRepositoryTest.java
@Test
@Transactional
@DisplayName("유저 추가 테스트")
public void createTest() {
    User user = new User.UserBuilder()
        .setName("test")
        .setPhoneNumber("010-1234-5678")
        .setPushAlarmAllow(false)
        .setProfileImage("src/test_profile.jpg")
        .setGender(Gender.M)
        .build();
    User newUser = userRepository.save(user);

    Optional<User> findUser = userRepository.findById(newUser.getId());

    Assertions.assertFalse(newUser.equals(findUser));
}
~~~
- 인스턴스 생성 시 각 인자가 무엇인지 알 수 있고(가독성이 좋다!)
- 필요한 인자만 넣어 생성할 수 있다!
- but 여전히 코드는 길다...

## Lombok @Builder

롬복 웨않쓺?

~~~java
// User.java
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    // User Entity 내부 컬럼 여러개 주저리주저리
    // 있다치고

    // 여기있던 아주 긴 코드
    // private 생성자와
    // static class UserBuilder가
    // @Builder만 써주는 것으로 대체되었다
}
~~~

~~~java
// UserRepositoryTest.java
@Test
@Transactional
@DisplayName("유저 추가 테스트")
public void createTest() {
    User user = User.builder()
        .name("test")
        .phoneNumber("010-1234-5678")
        .pushAlarmAllow(false)
        .profileImage("src/test_profile.jpg")
        .gender(Gender.M)
        .build();
    User newUser = userRepository.save(user);

    Optional<User> findUser = userRepository.findById(newUser.getId());

    Assertions.assertFalse(newUser.equals(findUser));
}
~~~