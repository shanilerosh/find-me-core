package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface PostService {

    Boolean createPost(MultipartFile multipartFile, PostDtoEmpl postDto);

    List<PostDto> fetchPosts(String empId);

    Boolean reactPost(String empId,String postId, String type);

    Boolean commentPost(String empId, String postId, CommentDto commentDto);

    List<CommentFrontDto> getComments(String postId);

    List<PostDto> fetchPostsByEmployee(Employee employee);

    List<PostDto> fetchFriendsPosts(String empId);
}
