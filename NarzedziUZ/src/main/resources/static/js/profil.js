document.addEventListener('DOMContentLoaded', function() {
    initProfile();
});

function initProfile() {
    // Hover efekty gwiazdek
    document.querySelectorAll('.star').forEach(star => {
        star.addEventListener('mouseenter', () => {
            star.style.transform = 'scale(1.2)';
        });
        star.addEventListener('mouseleave', () => {
            star.style.transform = 'scale(1)';
        });
    });

    // Search functionality
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');

    if (searchBtn && searchInput) {
        searchBtn.addEventListener('click', performSearch);
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') performSearch();
        });
    }

    // Purchase cards click - redirect to details
    document.querySelectorAll('.purchase-card').forEach(card => {
        card.addEventListener('click', function() {
            const purchaseId = this.id.replace('purchase-', '');
            window.location.href = `/user/purchase/${purchaseId}`;
        });
    });

    // Apply user config if available
    if (window.userConfig) {
        applyConfig(window.userConfig);
    }
}

function performSearch() {
    const query = document.getElementById('searchInput').value;
    const priceFilter = document.getElementById('priceFilter').value;
    const categoryFilter = document.getElementById('categoryFilter').value;

    const params = new URLSearchParams();
    if (query) params.append('q', query);
    if (priceFilter) params.append('price', priceFilter);
    if (categoryFilter) params.append('category', categoryFilter);

    window.location.href = `/search?${params.toString()}`;
}

function applyConfig(config) {
    if (!config) return;

    document.body.style.background = config.background_color || '#f5f7fa';

    const profileContainer = document.querySelector('.profile-container');
    if (profileContainer) {
        profileContainer.style.background = config.surface_color || '#ffffff';
    }

    const primaryColor = config.primary_action_color || '#0056b3';
    const textColor = config.text_color || '#1a202c';

    // Update dynamic elements
    document.querySelector('.profile-avatar').style.background = primaryColor;
    document.querySelector('.profile-avatar').style.color = config.surface_color || '#ffffff';

    document.querySelectorAll('.info-value').forEach(el => {
        el.style.color = primaryColor;
    });

    document.querySelectorAll('.purchase-status').forEach(el => {
        el.style.background = primaryColor;
    });

    document.querySelectorAll('.section-title').forEach(el => {
        el.style.borderBottomColor = primaryColor;
        el.style.color = textColor;
    });

    document.querySelectorAll('.item-price, .total-amount').forEach(el => {
        el.style.color = primaryColor;
    });
}
