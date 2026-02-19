package com.example.api.admin.batch

import com.example.base.provider.storage.StorageProvider
import com.example.base.utils.log
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.job.JobExecution
import org.springframework.batch.core.listener.JobExecutionListener
import org.springframework.batch.core.repository.JobRepository
import org.springframework.stereotype.Component
import java.io.File

@Component
class ExportUploadListener(
    private val storageProvider: StorageProvider,
    private val jobRepository: JobRepository
) : JobExecutionListener {

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.COMPLETED) {
            // 从 StepContext 或 JobParameters 中获取文件路径
            val filePath = jobExecution.stepExecutions.first().executionContext.getString("outputFilePath")
            val file = File(filePath)

            if (file.exists()) {
                try {
                    val key = storageProvider.upload(file)
                    // 将上传后的 URL 存入 JobContext，方便 Controller 获取
                    jobExecution.executionContext.putString("result", key)
                    jobRepository.updateExecutionContext(jobExecution)  // 显式更新到数据库
                    log.info("File uploaded successfully. URL: $key")
                }catch (e: Exception){
                    log.error("File upload failed: ${e.message}")
                } finally {
                    file.delete() // 务必清理本地临时文件
                }
            }
        }
    }
}
