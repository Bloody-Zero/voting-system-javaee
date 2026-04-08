// Подтверждение удаления
document.addEventListener('DOMContentLoaded', function() {
    // Все кнопки/ссылки с классом 'btn-danger' или data-confirm
    const deleteButtons = document.querySelectorAll('.delete-btn, .btn-danger[onclick]');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            if (!confirm('Вы уверены, что хотите удалить эту запись? Это действие необратимо.')) {
                e.preventDefault();
            }
        });
    });

    // Автоматическое скрытие alert-сообщений через 5 секунд
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // Валидация формы на клиенте (пример для пользователя)
    const userForm = document.querySelector('#userForm');
    if (userForm) {
        userForm.addEventListener('submit', function(e) {
            const email = document.getElementById('email');
            const phone = document.getElementById('phone');
            let errors = [];
            if (email && !/^[^\s@]+@([^\s@.,]+\.)+[^\s@.,]{2,}$/.test(email.value)) {
                errors.push('Некорректный email');
                email.style.borderColor = 'red';
            }
            if (phone && phone.value && !/^\+?[0-9]{10,15}$/.test(phone.value)) {
                errors.push('Телефон должен содержать 10-15 цифр, возможно с +');
                phone.style.borderColor = 'red';
            }
            if (errors.length > 0) {
                e.preventDefault();
                alert(errors.join('\n'));
            }
        });
    }
});