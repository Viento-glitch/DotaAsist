package ru.sa.dotaassist.client.dao;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.sa.dotaassist.client.UserApiResponse;

public interface UserService {
    @GET("/api/users/{id}")
    public Call<UserApiResponse> getUser(@Path("id") long id);

}
