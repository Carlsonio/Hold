package com.example.hold.di

import com.example.hold.data.NoteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.hold.data.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room

class SafeHelperFactory(private val passphrase: ByteArray) : SupportSQLiteOpenHelper.Factory {
    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        return net.sqlcipher.database.SupportFactory(passphrase).create(configuration)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext app: Context,
        // CryptoManager будет предоставлен Hilt (см. следующий шаг)
        cryptoManager: CryptoManager
    ): NoteDatabase {
        // Получаем ключ для шифрования БД
        val passphrase = cryptoManager.getDatabasePassphrase()
        val factory = SafeHelperFactory(passphrase)

        val db = Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "notes_encrypted.db" // Имя зашифрованного файла БД
        )
            .openHelperFactory(factory) // Указываем нашу фабрику
            .build()

        // Очищаем пароль из памяти для безопасности
        passphrase.fill(0)

        return db
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDatabase): NoteDao {
        return db.noteDao()
    }

    // Предоставляем репозиторий
    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(dao)
    }
}