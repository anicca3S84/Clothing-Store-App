package com.dailycode.clothingstore.security.config;

import com.dailycode.clothingstore.security.jwt.AuthTokenFilter;
import com.dailycode.clothingstore.security.jwt.JwtAuthEntryPoint;
import com.dailycode.clothingstore.security.user.ShopUserDetails;
import com.dailycode.clothingstore.security.user.ShopUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity //kích hoạt cấu hình bảo mật web trong Spring Security
@Configuration //Đánh dấu lớp là một lớp cấu hình trong Spring
@EnableMethodSecurity(prePostEnabled = true)//Annotation này kích hoạt bảo mật ở mức độ phương thức trong Spring Security.
//cho phép sử dụng các annotation như @PreAuthorize và @PostAuthorize để
// kiểm tra quyền truy cập trước và sau khi phương thức được gọi.
public class Config {
    @Autowired
    private ShopUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthEntryPoint authEntryPoint;

    private static  final List<String> SECURED_URLS = List.of(
            "/api/v1/carts/**", "/api/v1/cartItems/**"
    );

    //@Bean có thể được sử dụng trong các lớp được đánh dấu với
    // @Configuration hoặc @Component
    @Bean
    // được sử dụng để đánh dấu một phương thức tạo ra
    // một đối tượng mà Spring IoC container sẽ quản lý.
    // Khi phương thức được đánh dấu với @Bean, Spring
    // sẽ gọi phương thức đó và đăng ký đối tượng trả
    // về dưới dạng một bean trong container.
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    //AuthenticationManager: Là một interface trong Spring Security chịu trách nhiệm xử lý các yêu cầu xác thực.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }


    //DaoAuthenticationProvider là một triển khai của AuthenticationProvider sử dụng UserDetailsService và PasswordEncoder
    // để xác thực tên người dùng và mật khẩu.
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //không sử dụng cookie cho xác thực.
        //Vô hiệu hóa cấu hình CSRF bằng cách sử dụng phương thức disable của AbstractHttpConfigurer
        http.csrf(AbstractHttpConfigurer::disable)
                //cấu hình điểm nhập cho các ngoại lệ xác thực, giúp xử lý các trường hợp không xác thực.
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                //Điều này có nghĩa là Spring Security sẽ không tạo mới hoặc sử dụng
                // bất kỳ phiên HTTP nào để lưu trữ thông tin xác thực của người dùng.
                // Mỗi yêu cầu HTTP sẽ được xử lý độc lập mà không phụ thuộc vào bất kỳ
                // trạng thái phiên nào.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // cấu hình các URL được bảo vệ yêu cầu người dùng phải xác thực, trong khi các yêu cầu khác được phép truy cập mà không cần xác thực.
                .authorizeHttpRequests(auth ->auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
                        .anyRequest().permitAll());
        // thiết lập nhà cung cấp xác thực sử dụng DaoAuthenticationProvider,
        // cho phép xác thực người dùng từ cơ sở dữ liệu.
        http.authenticationProvider(daoAuthenticationProvider());
        //chèn một bộ lọc tùy chỉnh (authTokenFilter) vào chuỗi bộ lọc bảo mật,
        // đảm bảo rằng bộ lọc này được thực thi trước
        // bộ lọc UsernamePasswordAuthenticationFilter.
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
