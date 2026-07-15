import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 创建运营广告请求
 */
@Data
public class AdvertisingAddRequest implements Serializable {

    /** 运营位 id */
    private Long spaceId;

    /** 广告标题 */
    private String name;

    /** 封面图 */
    private String cover;

    /** 视频地址 */
    private String video;

    /** 跳转路径 */
    private String path;

    /** 详情内容 */
    private String content;

    /** 备注/简介 */
    private String remark;

    /** 排序 */
    private Integer sort;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 开始展示日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    /** 结束展示日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    private static final long serialVersionUID = 1L;
}
