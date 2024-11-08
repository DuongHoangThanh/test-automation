import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages(".")  // Thư mục gốc chứa các kiểm thử
@IncludeClassNamePatterns(".*Tests")  // Chỉ định mẫu tên kiểm thử cần chạy (vd: *_Tests.java)
public class TestSuiteRunner {
    // Lớp này sẽ tự động tìm và chạy tất cả các kiểm thử có tên phù hợp với mẫu trong thư mục hiện tại
}

