package co.dev.oauth.oauth.security;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStrore2());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().disable()
                .authorizeRequests().antMatchers("/oauth/**").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.GET,"/playlist/listar").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.GET,"playlist/ver/{name}").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.POST,"playlist/agregar").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.DELETE,"playlist/eliminar/{id}").hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated()
                .and().cors().configurationSource(corsConfigurationSource());
        ;
    }
    @Bean
    public  CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("*"));
        corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    @Primary
    public JwtTokenStore tokenStrore2() {
        return new JwtTokenStore(accestokenConverter2());
    }

    @Bean
    @Primary
    public JwtAccessTokenConverter accestokenConverter2() {
        JwtAccessTokenConverter tokennConverter = new JwtAccessTokenConverter();
        tokennConverter.setSigningKey("algun_codigo_secreto_aeiou");
        return tokennConverter;
    }

}
